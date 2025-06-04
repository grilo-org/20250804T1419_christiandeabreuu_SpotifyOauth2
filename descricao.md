# Spotify API

<aside>
ℹ️ Fique atento a todas as instruções que este documento oferece, a continuidade do processo dependerá disso!

</aside>

Você está na etapa do desafio técnico, parabéns por ter chegado até aqui! 
Neste desafio, queremos conhecer suas habilidades técnicas em foco prático e aplicado na resolução de um problema. 
Aqui conheceremos seu estilo de código, aptidões técnicas e, sobretudo, a sua capacidade de resolução de problemas.

# O desafio…

O nosso time gosta bastante de ouvir música, e com isso gostaríamos de ter um aplicativo que se comunicasse com o Spotify e nos mostrasse algumas informações de forma mais prática.

## Objetivo

Você deve criar uma aplicação que autentique com o Spotify e cumpra os seguintes casos de uso:

- Listar os artistas que mais ouvimos;
- Listar os álbuns desses artistas;
- Listar as playlists do usuário;
- Criar uma nova playlist;
- Exibir os dados do usuário.

## UI/Protótipo
dentro do projeto no arquivo figmaProjetoSpotify

# Instruções

A seguir estão os requisitos (obrigatórios e bônus) que serão levados em consideração para a análise da sua solução.
Ao submeter o projeto, inclua o texto abaixo em seu README, marcando tudo aquilo que realmente foi feito.
# Requisitos
## Requisitos obrigatórios
- [ ] Atenticação via Spotify
- [ ] Listar artistas
- [ ] Listar albuns de um artista
- [ ] Utilizar paginação (scroll infinito ou não)
- [ ] Funcionamento offline (manter dados em storage local)
- [ ] Testes unitários
- [ ] Seguimentação de commits

## Bônus
- [ ] Testes instrumentados
- [ ] Integração com Firebase (Crashlytics)
- [ ] CI/CD (pipelines e deploy)
- [ ] Responsividade (celular e tablet)

A solução do desafio deve seguir a interface proposta acima, e isso também será critério de avaliação.

## Spotify API

O desafio deverá obrigatoriamente utilizar a [API do Spotify](https://developer.spotify.com/documentation/web-api). Para faciltar o processo de implementação, deixaremos abaixo as rotas que serão utilizadas nos casos de uso:

- Listar os artistas que mais ouvimos
    
    [Web API Reference | Spotify for Developers](https://developer.spotify.com/documentation/web-api/reference/get-users-top-artists-and-tracks)
    
- Listar os álbuns desses artistas
    
    [Web API Reference | Spotify for Developers](https://developer.spotify.com/documentation/web-api/reference/get-an-artists-albums)
    
- Listar as playlists do usuário
    
    [Web API Reference | Spotify for Developers](https://developer.spotify.com/documentation/web-api/reference/get-a-list-of-current-users-playlists)
    
- Criar uma nova playlist
    
    [Web API Reference | Spotify for Developers](https://developer.spotify.com/documentation/web-api/reference/create-playlist)
    
- Exibir os dados do usuário
    
    [Web API Reference | Spotify for Developers](https://developer.spotify.com/documentation/web-api/reference/get-current-users-profile)
    

# TL;DR

1. A arquitetura fica a seu critério, mas serão avaliados os fatores: manutenabilidade, escalabilidade e desempenho.
2. É obrigatório o uso do [Android Nativo](https://developer.android.com/?hl=pt-br) para a resolução desse problema.
3. Escreva um README descrevendo o passo a passo de como executar a aplicação e o que é necessário para tal.
4. Deixe claro na documentação (README) as escolhas utilizadas, ao que tange tecnologia e padrões arquiteturais aplicados na resolução.
