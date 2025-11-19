# CLAUDE.md

このファイルは、Claude Code (claude.ai/code) がこのリポジトリで作業する際のガイダンスを提供します。

## プロジェクト概要

**StopWatchTimerRenewal** は、特定の時間間隔で音声チャイム（予鈴、終鈴、終了チャイム）を設定可能なモダンなAndroidストップウォッチ/タイマーアプリケーションです。このアプリは、100% Jetpack Composeを使用したクリーンアーキテクチャ原則に基づいて構築されています。

## ビルドコマンド

### APKのビルド
```bash
./gradlew assembleDebug
```

出力場所: `app/build/outputs/apk/debug/app-debug.apk`

### テストの実行
```bash
# ユニットテスト
./gradlew test

# インスツルメンテーションテスト
./gradlew connectedAndroidTest
```

### クリーンビルド
```bash
./gradlew clean build
```

### Android Studioでのデバッグビルド生成
- Run > Run 'app'
- または: Shift+F10

## アーキテクチャ概要

コードベースは、3つのレイヤーで明確に関心事を分離した**クリーンアーキテクチャ**に従っています。

### レイヤー構造

```
┌─────────────────────────────────────────────────────────┐
│ PRESENTATION (presentation/)                             │
│ - 画面 (Composables)                                     │
│ - ViewModels (Hiltによる)                                │
│ - UI State クラス                                        │
└─────────────────┬───────────────────────────────────────┘
                  │ StateFlowを監視
                  │ ユースケースを呼び出し
┌─────────────────▼───────────────────────────────────────┐
│ DOMAIN (domain/)                                         │
│ - モデル (TimerState, ChimeSettings, ChimeType)         │
│ - Repositoryインターフェース                             │
│ - Audio Playerインターフェース                           │
└─────────────────┬───────────────────────────────────────┘
                  │ 実装される
┌─────────────────▼───────────────────────────────────────┐
│ DATA (data/)                                             │
│ - Repository実装 (DataStore)                             │
│ - Audio Player実装 (MediaPlayer)                         │
└──────────────────────────────────────────────────────────┘
```

### パッケージ構成

- **`presentation/timer/`**: メインタイマー画面、ViewModel、UI状態
- **`presentation/settings/`**: 設定画面、ViewModel、時刻選択ダイアログ
- **`presentation/navigation/`**: ナビゲーショングラフと画面ルート
- **`domain/model/`**: コアドメインエンティティ (TimerState, ChimeSettings, ChimeType)
- **`domain/repository/`**: Repositoryインターフェース
- **`domain/audio/`**: Audio playerインターフェース
- **`data/repository/`**: DataStoreベースのRepository実装
- **`data/audio/`**: MediaPlayerベースのAudio実装
- **`di/`**: Hilt依存性注入モジュール

## 主要なアーキテクチャパターン

### 単方向データフロー (UDF)

すべての画面は以下のパターンに従います：
```
ユーザーアクション → ViewModelイベント → 状態更新 → UI再コンポーズ
```

- ViewModelsはUI状態のために**StateFlow**を公開
- 画面は`collectAsState()`で状態を収集
- ユーザーインタラクションはViewModel関数で処理（例：`onStartClicked()`）

### Hiltによる依存性注入

すべての依存関係はHiltを通じて提供されます：
- **Application**: `StopWatchTimerApplication`は`@HiltAndroidApp`でアノテート
- **Activity**: `MainActivity`は`@AndroidEntryPoint`でアノテート
- **ViewModels**: コンストラクタインジェクションで`@HiltViewModel`を使用
- **Modules**: `di/`パッケージに配置
  - `RepositoryModule`: `ChimeRepository`インターフェースを実装にバインド
  - `AudioModule`: `AudioPlayer`インターフェースを実装にバインド

### 状態管理

**TimerViewModel**はタイマー状態を以下で管理：
- 内部状態のための`MutableStateFlow<TimerUiState>`
- UIに公開される`StateFlow<TimerUiState>`
- タイマーティックループのためのCoroutine Job
- 重複した音声を防ぐための再生済みチャイムのSet

**SettingsViewModel**は設定状態を管理：
- リポジトリFlowから`ChimeSettings`を収集
- リポジトリメソッドを通じて設定を更新

### データの永続化

設定は**DataStore Preferences**を使用して永続化：
- FlowベースのAPIでリアクティブ
- プリファレンスキーで型安全
- 非同期（ノンブロッキング）
- 3つのキー：`first_bell_seconds`、`second_bell_seconds`、`end_chime_seconds`

## ナビゲーション構造

Compose Navigationを使用したシングルアクティビティアーキテクチャ：
- **開始画面**: `"timer"` → `TimerScreen`
- **設定ルート**: `"settings"` → `SettingsScreen`
- ルートは`Screen` sealedクラスで定義
- ナビゲーションは`AppNavigation.kt`で処理

## 音声システム

音声再生はAndroid MediaPlayerを使用：
- **リソース**: `R.raw.bell`と`R.raw.end_chime`
- **Audio Attributes**: `USAGE_ALARM`（アラーム音量を尊重）
- ライフサイクル: 作成 → 設定 → 再生 → 自動リリース
- `ViewModel.onCleared()`での手動クリーンアップ

**重要**: `app/src/main/res/raw/`の音声ファイルは現在プレースホルダーです。実際のmp3ファイルに置き換えてください：
- `bell.mp3` - 予鈴と終鈴で使用
- `end_chime.mp3` - 終了チャイムで使用

## タイマーロジック

タイマーはコルーチンベースのループで動作：

1. **Start**: `while(true)`ループでコルーチンを起動
2. **Tick**: 毎秒、`elapsedSeconds`をインクリメント
3. **Chime Check**: 現在の秒数を設定と比較
4. **Audio Trigger**: 時間が一致し、まだ再生されていない場合にチャイムを再生
5. **Stop**: Coroutine Jobをキャンセル、経過時間を保持
6. **Reset**: Jobをキャンセル、0秒のIdle状態にリセット

チャイムはSetで追跡され、1つのセッション中の重複再生を防止します。

## 文字列リソース

すべてのユーザー向け文字列は`app/src/main/res/values/strings.xml`で定義する必要があります：
- Composablesで`stringResource(R.string.key_name)`を使用
- Kotlinファイル内で文字列をハードコードしない
- 国際化をサポート

## UIテーマ

- **Theme**: Material3とダイナミックカラーサポート（Android 12+）
- **Color scheme**: システム設定に基づく自動ダーク/ライトモード
- **Files**: `ui/theme/`に`Theme.kt`、`Color.kt`、`Type.kt`を含む

## 特殊機能

### 画面スリープ防止
`MainActivity`は`FLAG_KEEP_SCREEN_ON`を設定して、タイマー操作中の画面スリープを防止します。

### ボタン状態管理
ボタンは`TimerState`に基づいて有効/無効化されます：
- **Start**: `Idle`または`Stopped`のときに有効
- **Stop**: `Running`のときに有効
- **Reset**: `Idle`でないときに有効

## 開発ガイドライン

### 新機能を追加する場合

1. **Models**: `domain/model/`に追加
2. **Interfaces**: `domain/repository/`または`domain/audio/`に追加
3. **Implementations**: 対応する`data/`サブディレクトリに追加
4. **DI**: `di/`でHiltモジュールを作成または更新
5. **UI**: `presentation/`に画面を追加、`@HiltViewModel`でViewModelsを追加

### UIを修正する場合

- テーマの既存のMaterial3コンポーネントを使用
- 既存の状態管理パターンに従う（StateFlow）
- 単方向データフローを維持
- 再利用可能なコンポーネントを別の`@Composable`関数として抽出

### DataStoreを使用する場合

- すべてのリポジトリ操作はリアクティブ更新のために`Flow`を返す
- ViewModelsは`init`ブロックでFlowを収集し、StateFlowに変換
- 設定変更はすべてのオブザーバーに自動的に伝播

### 依存関係を追加する場合

- `gradle/libs.versions.toml`（バージョンカタログ）に追加
- `app/build.gradle.kts`で`libs.`プレフィックスを使用して参照
- 必要に応じてDIモジュールを更新

## プロジェクト構成

- **Min SDK**: 30 (Android 11.0)
- **Target SDK**: 36
- **Compile SDK**: 36
- **Java Version**: 11
- **Kotlin**: 2.0.21
- **Compose BOM**: 2024.09.00
- **Package**: `com.nokopi.stopwatchtimer`

## ドキュメント

`docs/`ディレクトリを参照：
- **アプリ仕様書.md**: 完全なアプリ仕様（日本語）
- **実装完了.md**: 実装チェックリストとアーキテクチャ概要

## 避けるべき一般的な落とし穴

1. **文字列をハードコードしない** - 常に文字列リソースを使用
2. **LiveDataを使用しない** - このプロジェクトはStateFlowを使用
3. **Hiltをバイパスしない** - すべての依存関係は注入されるべき
4. **メインスレッドをブロックしない** - 非同期操作にはコルーチンを使用
5. **ライフサイクルを無視しない** - `onCleared()`でリソースをクリーンアップ
6. **UDFをスキップしない** - すべての状態更新はViewModelを通過

## 従うべきコードパターン

### ViewModelパターン
```kotlin
@HiltViewModel
class MyViewModel @Inject constructor(
    private val repository: SomeRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(MyUiState())
    val uiState: StateFlow<MyUiState> = _uiState.asStateFlow()

    fun onUserAction() {
        // 状態を更新
    }

    override fun onCleared() {
        // リソースをクリーンアップ
    }
}
```

### Screenパターン
```kotlin
@Composable
fun MyScreen(
    onNavigateSomewhere: () -> Unit,
    viewModel: MyViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = { TopAppBar(...) }
    ) { padding ->
        // uiStateに基づくUI
    }
}
```

### Repositoryパターン
```kotlin
interface MyRepository {
    fun getData(): Flow<MyData>
    suspend fun updateData(data: MyData)
}

class MyRepositoryImpl @Inject constructor(
    private val dataStore: DataStore<Preferences>
) : MyRepository {
    override fun getData(): Flow<MyData> = dataStore.data.map { /* ... */ }
    override suspend fun updateData(data: MyData) { /* ... */ }
}
```

## テスト戦略

現在のテストインフラストラクチャは最小限：
- ユニットテストは`app/src/test/`に配置
- インスツルメンテーションテストは`app/src/androidTest/`に配置
- Compose UIテストは`createComposeRule()`を使用可能
- ViewModelsはコルーチンテストディスパッチャーでテストする必要あり
