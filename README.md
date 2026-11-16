# Treino Dieta

App Android nativo (Kotlin + Jetpack Compose) para registro de treinos (musculação + aeróbico) e dieta, uso pessoal e 100% offline. MVP em evolução por fases — aparelho alvo: Samsung A32 (4 GB RAM).

## Stack (versões verificadas em nov/2026)

| Componente | Versão |
|---|---|
| Kotlin (+ plugin Compose) | 2.3.21 |
| Android Gradle Plugin | 8.13.2 |
| Gradle | 8.14.7 |
| KSP | 2.3.14 |
| Compose BOM (Material 3) | 2026.06.01 |
| Room | 2.8.5 |
| Navigation Compose | 2.9.6 |
| Lifecycle (runtime/viewmodel-compose) | 2.10.0 |
| Activity Compose | 1.13.0 |
| DataStore Preferences | 1.3.0 |
| Vico (gráficos, Fase 4) | 3.2.3 |

`minSdk 26` (java.time nativo), `compileSdk/targetSdk 36`, R8 + shrinkResources no release. Bibliotecas mais novas (Lifecycle 2.11, Navigation 2.10.2+, Core 1.19+, Vico 3.3+, Compose ui 1.12+) exigem compileSdk 37 / AGP 9 — atualizar tudo em bloco quando migrar.

## Estrutura

```
app/src/main/java/com/felipe/treinodieta/
├── MainActivity.kt            # única Activity; tudo é Compose
├── TreinoDietaApplication.kt  # cria o AppContainer
├── di/AppContainer.kt         # injeção manual (banco + repositórios + DataStore)
├── data/
│   ├── local/                 # entidades Room, DAOs e AppDatabase
│   ├── repository/            # TreinoRepository, DietaRepository
│   └── settings/              # MetaRepository (DataStore: metas diárias)
└── ui/
    ├── TreinoApp.kt           # Scaffold + barra inferior (4 abas) + NavHost
    ├── theme/                 # cores, tipografia, tema Material 3
    ├── components/            # componentes reutilizáveis
    └── screens/               # Hoje, Treinos, Dieta, Progresso (+ telas das Fases 1-4)
```

## Fases do MVP

0. ✅ **Esqueleto**: navegação com 4 abas, banco criado, repositórios e metas prontos
1. **Treinos**: criar/editar sessão, exercícios com séries (reps + carga), cardio, histórico
2. **Dieta**: biblioteca de alimentos, refeições com itens (snapshot nutricional), metas
3. **Dashboard "Hoje"**: resumo do dia vs. meta, totais semanais (volume e kcal/macros)
4. **Gráficos** (Vico): carga por exercício ao longo do tempo; kcal/macros por semana
5. **Polimento**: validação mínima, confirmações, export/import de backup JSON

## Como rodar

1. Abra a pasta no Android Studio (Otter 2025.2 ou mais novo) e aguarde o sync baixar o Gradle e as dependências.
2. Conecte o celular via USB com **depuração USB** ativa (Opções do desenvolvedor). Sem emulador — o PC tem 8 GB de RAM.
3. `Run ▶` no Android Studio, ou pelo terminal: `./gradlew installDebug`.

### PC com 8 GB RAM

O `gradle.properties` já limita o heap do Gradle a 2 GB. Durante builds, feche o navegador e outros apps pesados. Primeiro build: ~3–6 min; incrementais: ~30 s.

## Decisões de arquitetura (resumo do plano)

- **Room** como fonte de dados (dados relacionais + agregações SQL + Flow reativo). Nuvem só no futuro, mantendo Room: backup JSON → Supabase → Firestore.
- **Datas como `epochDay` (Long)**: consultas por dia/semana viram comparações numéricas simples.
- **MVVM sem camada de domínio**: UI (Compose) → ViewModel (StateFlow) → Repositório → DAO. Casos de uso só aparecerem se o app crescer.
- **Snapshot nutricional** em `ItemRefeicao`: editar a biblioteca de alimentos nunca altera o histórico.
- **Metas** no DataStore Preferences; todo o resto no Room.
- **Uma tabela única de registros de treino** (séries e cardio, campos nulos onde não se aplicam); alternativa futura: separar em duas tabelas.
