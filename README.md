<h1 align="center" style="font-weight: bold;">FreeLoot API💻</h1>

<p align="center">
 <a href="#tech">Tecnologias</a> • 
 <a href="#started">Começando</a> • 
  <a href="#routes">Endpoints da API</a> •
 <a href="#proximas-features">Próximas features</a> •
 <a href="#colab">Colaboradores</a> •
 <a href="#contribute">Contribuindo</a> •
 <a href="#Licença">Licença</a>
</p>

<p align="center">
    <b>
        FreeLoot API é uma aplicação que realiza scraping de dados de sites como a Epic Games Store para obter informações sobre jogos gratuitos e disponibilizá-los por meio de uma API RESTful. O objetivo é capturar e salvar dados de jogos gratuitos em um banco de dados, permitindo o acesso a essas informações de forma estruturada.
    </b>
    <b>
        O Sistema está configurado para buscar Jogos que estão grátis todos os dias ás 05:00 da manhã.
    </b>
</p>

<h2 id="technologies">💻 Tecnologias</h2>

- Java 17
- Spring Boot 3
- Spring Data JPA
- PostgreSQL
- Gradle
- FlyWay Migrations
- Jsoup
- Selenium WebDriver
- ChromeDriver

<h2 id="started">🚀 Começando</h2>

<h3>Pre-Requisitos</h3>

- [Java 17+](https://www.java.com/pt-BR/download/manual.jsp)
- [Gradle](https://gradle.org/install/)
- [Postgres](https://www.postgresql.org/)
- Chrome instalado (necessário para o Selenium)
- ChromeDriver instalado e disponível no PATH

<h3>Clonando o projeto</h3>

Cole a url abaixo para clonar o projeto
```bash
git clone https://github.com/CristianoMends/FreeLoot-API
```

Crie o banco de dados PostgreSQL:

```sql
CREATE DATABASE freeloot_db;
```

<h3>Configure as variaveis de ambiente</h2>

```yaml
   url: ${URL}
   username: ${USER}
   password: ${PASS}
```

<h2 id="routes">📍 Endpoints da API</h2>

| Route                  | Description                                                        
|------------------------|--------------------------------------------------------------------
| <kbd>GET api/always</kbd> | Lista os jogos salvos que são sempre grátis.                       |
| <kbd>GET api/weekly</kbd> | Lista os jogos salvos que são grátis na semana.                    |
| <kbd>GET api/expired</kbd> | Lista os jogos semanais salvos que já expiraram a semana gratuita. |


### RESPONSE

```json
[
  {
    "image": "https://imageurl",
    "name": "Game Name",
    "description": "Game Description",
    "status": "GRÁTIS",
    "period": "Indeterminado",
    "link": "https://gamelink",
    "updateDate": "2024-10-07T16:00:18.5804243"
  }
]
```

# Proximas Features

- Interface Gráfica
- Notificação por Email
- Buscar jogos em outras plataformas

<h2 id="colab">🤝 Colaboradoes</h2>

<table>
  <tr>
    <td align="center">
      <a href="https://github.com/CristianoMends">
        <img src="https://avatars.githubusercontent.com/u/116528159?v=4" width="100px;" alt="Cristiano Mendes Profile Picture"/><br>
        <sub>
          <b>Cristiano Mendes</b>
        </sub>
      </a>
    </td>
  </tr>
</table>

<h2 id="contribute">📫 Contribuindo</h2>


1. `git clone https://github.com/CristianoMends/FreeLoot-API`
2. `git checkout -b feature/NOME-DA-FUNCIONALIDADE`
3. Siga os padrões de commit.
4. Abra um Pull Request explicando o problema resolvido ou recurso realizado, se existir, anexe screenshot das modificações visuais e aguarde a revisão!

## Licença
Este projeto é licenciado sob os termos da [MIT Licence](license).