# Spotify App 🎵

## Descrição
Este é um aplicativo Android que utiliza a API do Spotify para oferecer funcionalidades personalizadas, como criação de playlists, exibição de artistas favoritos e muito mais. O objetivo principal é entregar uma experiência fluida e eficiente para os amantes de música.

## Funcionalidades Principais
- **Login e autenticação**: Integração com a API do Spotify para autenticar o usuário de forma segura, utilizando o protocolo **OAuth2**.
- **Top Artistas**: Exibição dos artistas mais ouvidos, com suporte à paginação para navegação fluida.
- **Albuns**: Exibição dos albuns dos artistas mais ouvidos
- **Playlists**: Visualização e gerenciamento de playlists do usuário.
- **Criação de Playlists**: Crie playlists diretamente no app com apenas alguns toques.
- **Perfil do Usuário**: Armazenamento de dados localmente para carregamento rápido.
- **Banco de Dados Local**: funcionamento do app em modo offline
  
## Escolhas de tecnologia e padrões arquiteturais

- **Kotlin**: Linguagem oficial recomendada para Android
- **MVVM (Model-View-ViewModel)**: Arquitetura escolhida para desacoplar regra de negócio, lógica de UI e acesso a dados, promovendo testabilidade e escalabilidade.
- **Clean Architecture**: Modularidade e separação de responsabilidades em camadas:
- **ViewModel e LiveData**: Para gerenciamento de ciclo de vida e reatividade das telas.
- **Android Jetpack**: Incluindo Paging para suporte a grandes listas de dados.
- **Retrofit**: Para requisições HTTP de forma segura e tipada.
- **Coroutines**: Para gerenciamento de chamadas assíncronas e concorrência.
- **SharedPreferences**: Persistência simples dos tokens de autenticação para manter sessão do usuário.
- **Room**: funcionamento offline do app e persistência de dados localmente.
- **Navigation Component**: Para navegação segura e desacoplada entre telas.
- **Material Design**: Para uma interface moderna e responsiva.
- **Testes unitários**: Cobertura utilizando o MockK , Junit, para simular dependências e validar a lógica das funcionalidades.
- **Espresso** Framework para testes de UI
- **GitHub Actions**: Pipeline de CI/CD para build, testes e geração de artefatos.
- **Firebase Crashlytics**: (Se implementado) para monitoramento de crashes em produção.

---

## Requisitos

### Requisitos obrigatórios
- [x] Autenticação via Spotify
- [x] Listar artistas
- [x] Listar albuns de um artista
- [x] Utilizar paginação (scroll infinito ou não)
- [x] Funcionamento offline (manter dados em storage local)
- [x] Testes unitários
- [x] Seguimentação de commits

### Bônus
- [x] Testes instrumentados
- [x] Integração com Firebase (Crashlytics)
- [x] CI/CD (pipelines e deploy)
- [x] Responsividade (celular e tablet)

## Passo a passo para executar a aplicação
1. Clone este repositório em sua máquina local. rodar na branch Master
2. Crie uma conta no [Spotify Developer Console](https://developer.spotify.com/dashboard/) para obter suas credenciais (Client ID e Client Secret).
3. Configure as credenciais no app.
4. Adicione o seu `client_id` e `client_secret` no arquivo `local.properties` ou utilize variáveis de ambiente apropriadas.
    No arquivo `BuildConfig`, garanta que as chaves SPOTIFY_CLIENT_ID e SPOTIFY_CLIENT_SECRET estejam corretas.
5. Compile o projeto e execute no emulador ou em um dispositivo físico Android.





