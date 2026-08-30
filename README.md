# Manuale MX-5 — app Android Auto

App per consultare il manuale uso e manutenzione della Mazda MX-5 direttamente da Android Auto: navigazione completa con la rotellina (nessun tocco richiesto) e ricerca testuale con tastiera nativa.

## Funzioni

- **Home** — 9 argomenti di manutenzione più utili (livelli, pneumatici, fusibili, batteria scarica), pronti all'apertura dell'app
- **Indice A-Z** — tutte le 421 voci del manuale, in ordine alfabetico, con conteggio per lettera
- **Ricerca testuale** — tastiera nativa Android Auto, risultati filtrati in tempo reale
- **Dettaglio** — testo della pagina del manuale corrispondente, in righe scorrevoli con la rotellina
- Icona di avviso (▲) accanto alle voci relative a spie e segnali acustici

## Limiti

- Il contenuto del manuale è mostrato come testo a righe scorrevoli, non come pagina PDF impaginata — è il formato con cui Android Auto permette la consultazione con la rotellina
- Il testo di dettaglio è troncato a 70 righe per voce; oltre quel limite compare un rimando alla pagina del manuale cartaceo
- Lo splash screen a schermo intero compare solo nei primi 1,6 secondi dopo l'apertura dell'app, poi passa automaticamente alla Home (pulsante "Salta" per saltarlo subito)

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

## Installazione (sideload)

1. Attiva "Origini sconosciute" sul telefono
2. Attiva la modalità sviluppatore in Android Auto (10 tocchi sulla versione, nelle impostazioni Android Auto) e abilita "Sorgenti sconosciute"
3. Installa l'APK sul telefono
4. Collega il telefono all'head unit: l'app compare nel launcher di Android Auto
