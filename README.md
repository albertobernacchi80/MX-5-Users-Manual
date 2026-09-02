# Manuale MX-5 — app Android Auto

App per consultare il manuale uso e manutenzione della Mazda MX-5, sia da Android Auto (solo rotellina, nessun tocco richiesto) sia direttamente dal telefono (interfaccia touch normale).

## Funzioni

- **Home** — 9 argomenti di manutenzione più utili (livelli, pneumatici, fusibili, batteria scarica), pronti all'apertura dell'app
- **Indice A-Z** — tutte le voci del manuale in ordine alfabetico, con conteggio per lettera
- **Ricerca testuale** — tastiera nativa (Android Auto o telefono), risultati filtrati in tempo reale
- **Dettaglio** — testo integrale della pagina del manuale corrispondente alla voce
- Icona di avviso (▲) accanto alle voci relative a spie e segnali acustici
- Etichette delle voci fedeli al testo originale del manuale

## Su Android Auto

- Navigazione interamente con la rotellina: Home → Lettera → Dettaglio, oppure Ricerca
- Nel Dettaglio, il testo è diviso in pagine: azione "Pagina succ. ▶" per scorrere, "Indietro" per tornare
- Splash screen a schermo intero all'apertura (1,6 secondi, saltabile)

## Sul telefono

- Stessa Home, stesso indice, stessa ricerca
- Nel Dettaglio il testo è integrale e scorre a tocco, senza paginazione

## Limiti

- Il contenuto del manuale è mostrato come testo, non come pagina PDF impaginata
- Con l'auto in movimento (non in Park), Android Auto disabilita la tastiera di ricerca per sicurezza — è un comportamento del sistema, non dell'app
- Alcune voci molto generiche (es. "Sostituzione", "Specifiche") compaiono più volte nell'indice: sono voci distinte del manuale originale, riconoscibili dal numero di pagina mostrato accanto

## Compilazione

### Con Android Studio

1. Apri la cartella `mx5manual` in Android Studio (File → Open)
2. Lascia sincronizzare Gradle
3. Build → Build Bundle(s)/APK(s) → Build APK(s)
4. L'APK si trova in `app/build/outputs/apk/debug/app-debug.apk`

### Con GitHub Actions (senza Android Studio)

Il progetto include `.github/workflows/build-apk.yml`: ogni push su `main`/`master`, o avvio manuale, compila l'APK su un runner GitHub e lo carica come artifact scaricabile con il nome "MX-5 Users Manual.apk".

**Nuovo repository:**
1. Su github.com crea un nuovo repository
2. "Add file" → "Upload files", trascina il contenuto della cartella `mx5manual` (il file `build.gradle.kts` deve stare nella radice del repo)
3. "Commit changes"
4. Scheda "Actions": il workflow "Build APK" parte da solo (2-4 minuti)
5. A build completata, scarica l'artifact "MX5-UsersManual-debug"

**Repository esistente, solo il workflow:**
1. "Add file" → "Create new file"
2. Nome file: `.github/workflows/build-apk.yml`
3. Incolla il contenuto del file, poi "Commit new file"
4. Per rilanciare manualmente: scheda "Actions" → "Build APK" → "Run workflow"

## Installazione

Questa è un'app non distribuita su Google Play: va installata manualmente.

Servono [KingInstaller](https://github.com/fcaronte/KingInstaller/releases) e la modalità sviluppatore di Android Auto abilitata ([guida](https://www.smartworld.it/guide/come-abilitare-opzioni-sviluppatore-android-auto.html)).

1. Scarica l'APK
2. Attiva "Origini sconosciute" sul telefono
3. Attiva la modalità sviluppatore in Android Auto e abilita "Sorgenti sconosciute"
4. Installa l'APK con KingInstaller
5. Collega il telefono all'head unit: l'app compare nel launcher di Android Auto — oppure aprila direttamente dal telefono
