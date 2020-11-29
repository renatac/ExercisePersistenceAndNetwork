# App: Meus Climas Favoritos

- Projeto Android;
- Uso do Kotlin (Linguagem oficial do Google para desenvolvimento de Apps android);
- Criação de activities e Fragments;
- Layouts: LinearLayout e ConstraintLayout;
- Uso do RadionGroup, RadioButton, EditText, Button, CardView, ImageView, BottomNavigationView e RecyclerView; 
- Implementação do modo swipe para deletar elemento da recyclerview;
- Criação de uma tela de Splash;
- Uso das flags(Intent.FLAG_ACTIVITY_NEW_TASK e Intent.FLAG_ACTIVITY_CLEAR_TASK) na Activity de Splash: para que MainActivity se torne o início de 
uma nova tarefa na pilha de histórico e para que qualquer tarefa existente que seria associada a atividade a seja limpa antes de iniciar a MainActivity,
ou seja, para que quando o usuário aperte em voltar ele saia do App ao invés de voltar para a tela de Splash;
- Uso do ProgressBar em estilo horizontal e também em estilo circular;
- Criação de uma BaseActivity para uso da função comum setupToolbar entre as activities;
- Criação de uma Toolbar commum para ser incluída nos xml das activities;
- Aplicação de background shape criado para os botões;
- Persistência com SharedPreferences para: salvar e recuperar a cidade (digitada pelo usuário para a busca) e a linguagem (escolhida pelo o usuário 
para o App utilizar);
- Persistência de dados utilizando a biblioteca Room para: inserir, recuperar e deletar cidades dos favoritos e cidades pesquisadas;
- Alteração do idioma do aplicativo, feita através da escolha do usuário. Uso do método setLocale do package android.content.res; 
- Aplicação de um Style geral criado para uso comum de todos TextView da aplicação;
- Material Design;
- Uso do Bottom Navigation;
- Uso do Glide como framework de carregamento de imagens;
- Uso da Classe ConnectivityManager para o caso de exibição de um Toast informando ao usuário que o device está “Offline" (No internet connection);
- Consumo de API com Retrofit;
- Busca em http://api.openweathermap.org/data/2.5/weather?id=__&appid=__ para retornar a cidade através do id da cidade favoritada pelo usuário;
- Busca em http://api.openweathermap.org/data/2.5/find?q=__&units=metrics&appid=__ para encontrar a cidade digitada pelo usuário no campo de busca;
- Uso do Clean code e boas práticas de programação.
