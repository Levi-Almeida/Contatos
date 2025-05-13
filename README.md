# 📱 App Lista de Contatos Android

![Android](https://img.shields.io/badge/Android-3DDC84?style=for-the-badge&logo=android&logoColor=white)
![Java](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=java&logoColor=white)
![Retrofit](https://img.shields.io/badge/Retrofit-2.9.0-brightgreen)

## 📋 Descrição

Este aplicativo de Lista de Contatos foi desenvolvido como parte de um projeto acadêmico para demonstrar a integração com APIs REST utilizando a biblioteca Retrofit. O aplicativo permite gerenciar uma lista de contatos através da API `https://api-contacts-qfxx.onrender.com/docs/`, oferecendo funcionalidades completas de CRUD (Create, Read, Update, Delete).

## ✨ Funcionalidades

- **Visualização de Contatos**: Exibe uma lista de todos os contatos armazenados na API
- **Filtro de Favoritos**: Permite filtrar para visualizar apenas contatos marcados como favoritos
- **Adição de Contatos**: Interface para cadastrar novos contatos na lista
- **Edição de Contatos**: Possibilidade de modificar informações de contatos existentes
- **Exclusão de Contatos**: Remoção de contatos da lista com confirmação
- **Favoritos**: Marcar/desmarcar contatos como favoritos
- **Ligação Direta**: Função para ligar diretamente para um contato a partir do aplicativo

## 🚀 Tecnologias Utilizadas

- **Android Studio**: IDE para desenvolvimento Android
- **Java**: Linguagem de programação utilizada
- **Retrofit**: Biblioteca para consumo de APIs REST
- **RecyclerView**: Para exibição eficiente da lista de contatos
- **Material Design**: Componentes visuais modernos seguindo as diretrizes de design do Google

## 📂 Estrutura do Projeto

```
com.example.listacontatos/
├── activity/
│   ├── MainActivity.java       # Atividade principal com lista de contatos
│   └── FormContatoActivity.java # Formulário para adicionar/editar contatos
├── adapter/
│   └── ContatoAdapter.java     # Adaptador para RecyclerView
├── api/
│   ├── RetrofitConfig.java     # Configuração do cliente Retrofit
│   └── ContatoService.java     # Interface de serviços da API
├── model/
│   └── Contato.java            # Modelo de dados para contatos
└── res/
    ├── layout/
    │   ├── activity_main.xml         # Layout da tela principal
    │   ├── activity_form_contato.xml # Layout do formulário de contatos
    │   └── item_contato.xml          # Layout de cada item da lista
    ├── menu/
    │   └── menu_main.xml             # Menu da tela principal
    └── values/
        └── strings.xml               # Strings do aplicativo
```

## 🔧 Instalação e Configuração

1. Clone este repositório:
```bash
git clone https://github.com/seu-usuario/lista-contatos-android.git
```

2. Abra o projeto no Android Studio

3. Sincronize o Gradle e instale as dependências necessárias

4. Execute o aplicativo em um emulador ou dispositivo físico

## 📊 API Utilizada

O aplicativo consome a API de contatos disponível em:
- URL Base: `https://api-contacts-qfxx.onrender.com`
- Documentação: `https://api-contacts-qfxx.onrender.com/docs/`

Endpoints principais:
- `GET /contacts` - Lista todos os contatos
- `GET /contacts?favorite=true` - Lista apenas contatos favoritos
- `POST /contacts` - Adiciona um novo contato
- `PUT /contacts/{id}` - Atualiza um contato existente
- `DELETE /contacts/{id}` - Remove um contato

## 🧩 Recursos Adicionais

- Tratamento adequado de erros de conexão
- Feedback visual para ações do usuário
- Design responsivo para diferentes tamanhos de tela
- Validação de dados no formulário

## 📝 Requisitos do Projeto Acadêmico

Este projeto atende aos seguintes requisitos:
- Utilização da biblioteca Retrofit para consumo de API
- Criação de modelo para representação dos dados
- Exibição de lista de contatos na tela inicial
- Tela para cadastro e edição de contatos
- Funcionalidade de exclusão de contatos
- Funcionalidade para telefonar diretamente para um contato
- Filtro para buscar apenas contatos favoritos

## 🧪 Testes

Para testar o aplicativo:
1. Verifique se você possui conexão com a internet
2. Execute o aplicativo em um emulador ou dispositivo físico
3. Explore as funcionalidades de listar, adicionar, editar e excluir contatos

## 👨‍💻 Autor

*(Levi Santana de Almeida)*

## 📄 Licença

Este projeto está licenciado sob a [MIT License](LICENSE).

