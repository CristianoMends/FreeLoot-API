package com.api.freeloot.service.impl;

import com.api.freeloot.dto.GameView;
import com.api.freeloot.entity.Game;
import com.api.freeloot.repository.GameRepository;
import com.api.freeloot.service.SearchService;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SearchServiceImpl implements SearchService {
    private static final String EPIC_GAMES_URL = "https://store.epicgames.com";
    @Autowired
    private GameRepository gameRepository;

    @Autowired
    public List<GameView> getAllGamesAlwaysFree(){
        return this.gameRepository.getAllGamesAlwaysFree().stream().map(Game::toView).toList();
    }
    @Override
    public List<GameView> getAllWeeklyFree(){
        return gameRepository.findAllWeekly().stream().map(Game::toView).toList();
    }
    @Override
    public List<GameView> getAllExpired(){
        return gameRepository.findAllExpired().stream().map(Game::toView).toList();
    }
    @Scheduled(fixedRate = 380000)
    @Scheduled(cron = "0 0 5 * * ?", zone = "America/Sao_Paulo")
    public void saveAllWeeklyFreeGamesEpic() {
        WebDriver driver = null;
        try {
            ChromeOptions options = new ChromeOptions();
            driver = new ChromeDriver(options);

            driver.get(EPIC_GAMES_URL + "/pt-BR/free-games");
            driver.manage().window().minimize();

            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".css-17st2kc")));

            List<WebElement> gameElements = driver.findElements(By.cssSelector(".css-17st2kc"));
            var currentDateTime = ZonedDateTime.now(ZoneId.of("America/Sao_Paulo")).toLocalDateTime();

            for (WebElement gameElement : gameElements) {
                String name = gameElement.findElement(By.cssSelector("h6")).getText();
                if (gameRepository.existsByName(name)){continue;}

                var periodElements = gameElement.findElements(By.cssSelector("span"));
                String status = periodElements.remove(0).getText();
                if (!status.equals("GRÁTIS")) {
                    continue;
                }

                String image = gameElement.findElement(By.cssSelector("img")).getDomAttribute("src");
                String link = EPIC_GAMES_URL + gameElement.findElement(By.cssSelector("a")).getDomAttribute("href");

                String periodText = periodElements.stream()
                        .map(WebElement::getText)
                        .collect(Collectors.joining(" "));

                WebDriver gameDriver = null;
                try {
                    gameDriver = new ChromeDriver(options);
                    gameDriver.get(link);

                    WebDriverWait gameWait = new WebDriverWait(gameDriver, Duration.ofSeconds(30));
                    gameWait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".css-1myreog")));

                    String description = gameDriver.findElement(By.cssSelector(".css-1myreog")).getText();
                    description = (description.length() > 255) ? description.substring(0,250) + "...":description;
                    Game game = new Game(image, name, description, status, periodText, link, currentDateTime);

                    processGame(game);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (gameDriver != null) {
                        gameDriver.quit();
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (driver != null) {
                driver.quit();
            }
        }
    }


    @Scheduled(fixedRate = 580000)
    @Scheduled(cron = "0 0 5 * * ?", zone = "America/Sao_Paulo")
    public void getAllFreeGamesEpic() {//para buscar todos os jogos gratis da epic

        String gamesListSelector = ".css-lrwy1y";
        String gameNameSelector = ".css-lgj0h8";
        String gameImageSelector = "img";
        String gameLinkSelector = ".css-g3jcms";
        WebDriver driver = null;
        try {
            ChromeOptions options = new ChromeOptions();
            driver = new ChromeDriver(options);
            driver.get(EPIC_GAMES_URL+"/pt-BR/free-games");

            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
            var currentDateTime = ZonedDateTime.now(ZoneId.of("America/Sao_Paulo")).toLocalDateTime();

            wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.cssSelector(gamesListSelector)));

            List<WebElement> gameElements = driver.findElements(By.cssSelector(gamesListSelector));
            for (WebElement gameElement : gameElements) {
                String name = gameElement.findElement(By.cssSelector(gameNameSelector)).getText();

                if (gameRepository.existsByName(name)){continue;}

                String status = "GRÁTIS";
                String image = gameElement.findElement(By.cssSelector(gameImageSelector)).getDomAttribute("data-image");
                String link = EPIC_GAMES_URL + gameElement.findElement(By.cssSelector(gameLinkSelector)).getDomAttribute("href");
                String periodText = "Indeterminado";

                WebDriver gameDriver = null;
                try {
                    gameDriver = new ChromeDriver(options);
                    gameDriver.get(link);

                    WebDriverWait gameWait = new WebDriverWait(gameDriver, Duration.ofSeconds(30));
                    gameWait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".css-1myreog")));

                    String description = gameDriver.findElement(By.cssSelector(".css-1myreog")).getText();
                    description = (description.length() > 255) ? description.substring(0,250) + "...":description;
                    Game game = new Game(image, name, description, status, periodText, link, currentDateTime);

                    processGame(game);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (gameDriver != null) {
                        gameDriver.quit();
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            driver.quit();
        }
    }

    @Scheduled(cron = "0 0 5 * * ?", zone = "America/Sao_Paulo")
    public void checkGamesExpiration(){
        this.gameRepository.findAllWeekly().forEach(this::updateStatusIfExpired);
    }

    private void processGame(Game game) {
        Game existingGame = findExistingGame(game);
        if (existingGame != null) { return; }//já tem esse jogo entao sai
        gameRepository.save(game);
    }

    private void updateStatusIfExpired(Game game) {
        var now = ZonedDateTime.now(ZoneId.of("America/Sao_Paulo")).toLocalDateTime();
        var expirationDate = game.getUpdateDate().plusDays(7);

        if (now.isAfter(expirationDate)) {
            game.setStatus("ENCERRADO");
            this.gameRepository.save(game);
        }
    }

    private Game findExistingGame(Game game) {
        return gameRepository.findByName(game.getName())
                .stream()
                .findFirst()
                .orElse(null);
    }

}
