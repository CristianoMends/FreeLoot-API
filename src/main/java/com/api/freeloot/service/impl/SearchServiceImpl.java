package com.api.freeloot.service.impl;

import com.api.freeloot.dto.GameView;
import com.api.freeloot.entity.Game;
import com.api.freeloot.repository.GameRepository;
import com.api.freeloot.service.SearchService;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SearchServiceImpl implements SearchService {
    private static final String EPIC_GAMES_URL = "https://store.epicgames.com";
    @Autowired
    private GameRepository gameRepository;

    public List<GameView> getAll(){
        return this.gameRepository.findAll().stream().map(Game::toView).toList();
    }

    @Scheduled(cron = "0 0 5 * * ?", zone = "America/Sao_Paulo")
    public List<GameView> getFreeGamesEpic() {
        List<GameView> freeGamesWeekly = new ArrayList<>();

        WebDriver driver = null;
        try {
            driver = new ChromeDriver();
            driver.get(EPIC_GAMES_URL+"/pt-BR/free-games");
            driver.manage().window().minimize();

            Thread.sleep(4000);

            List<WebElement> gameElements = driver.findElements(By.cssSelector(".css-17st2kc"));
            for (WebElement gameElement : gameElements) {
                var periodElements = gameElement.findElements(By.cssSelector("span"));
                String status = periodElements.remove(0).getText();
                if (!status.equals("GRÁTIS")){
                    continue;
                }
                String name = gameElement.findElement(By.cssSelector("h6")).getText();
                String image = gameElement.findElement(By.cssSelector("img")).getDomAttribute("src");
                String link = EPIC_GAMES_URL + gameElement.findElement(By.cssSelector("a")).getDomAttribute("href");

                String periodText = periodElements.stream()
                        .map(WebElement::getText)
                        .collect(Collectors.joining(" "));

                GameView game = new GameView(image, name, status, periodText, link);
                freeGamesWeekly.add(game);
                saveGame(game.toEntity());
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // Fechando o WebDriver
            driver.quit();
        }

        return freeGamesWeekly;
    }

    @Scheduled(cron = "0 0 5 * * ?", zone = "America/Sao_Paulo")
    public void getAllFreeGamesEpic() {
        WebDriver driver = null;
        try {
            driver = new ChromeDriver();
            driver.get(EPIC_GAMES_URL+"/pt-BR/free-games");

            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

            wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.cssSelector(".css-lrwy1y")));

            List<WebElement> gameElements = driver.findElements(By.cssSelector(".css-lrwy1y"));
            for (WebElement gameElement : gameElements) {
                String status = "GRÁTIS"; // Considerando que o status está fixo
                String name = gameElement.findElement(By.cssSelector(".css-lgj0h8")).getText();
                String image = gameElement.findElement(By.cssSelector("img")).getDomAttribute("data-image");
                String link = EPIC_GAMES_URL + gameElement.findElement(By.cssSelector(".css-g3jcms")).getDomAttribute("href");

                String periodText = "Indeterminado";

                GameView game = new GameView(image, name, status, periodText, link);
                saveGame(game.toEntity());
                System.out.println(game);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            driver.quit();
        }
    }

    private void saveGame(Game game){
        var gameFound = gameRepository.findByName(game.getName());
        if (!gameFound.isEmpty()){return;}

        gameRepository.save(game);
    }
}
