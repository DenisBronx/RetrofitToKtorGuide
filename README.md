
# How to Safely Migrate from Retrofit to Ktor
## Step 1: Ensure Your Code is Well-Tested
Before starting the migration, it's crucial to ensure your code is fully tested and that your unit tests aren't overly coupled with implementation details. This will make the migration smoother and reduce the risk of regressions.

For testing network requests and responses, I recommend using NetMock. Unlike other stubbing libraries, NetMock is library-independent, meaning your tests remain decoupled from specific network libraries (like Retrofit or Ktor). This independence ensures that your tests are robust and won't need major rewrites during the migration.

If you're new to unit testing or need examples, check out this [sample test case](https://github.com/DenisBronx/RetrofitToKtorGuide/blob/retrofit-implementation/app/src/test/java/com/denisbrandi/migrateretrofit/data/repository/RealGithubProjectRepositoryTest.kt).

Advantages of Using NetMock for Testing:
* Library Independence: Keeps your tests free from network library-specific details.
* Simplifies Refactoring: Reduces the overhead of rewriting tests during migration.
* Flexible and Reliable: Works across different environments and libraries seamlessly.

## Step 2: Migrate from Retrofit to Ktor
With unit tests in place to protect your code, you can confidently start migrating from Retrofit to Ktor.

A key motivation for this migration is the transition to Kotlin Multiplatform (KMP). Since Moshi, the serialization library typically used with Retrofit, doesn’t support multiplatform projects, I also recommend migrating to Kotlinx Serialization for your data serialization needs.

Thanks to the unit tests, you can be confident that the migration process will be smooth and any potential issues will be quickly caught.

Migration Checklist:
* Swap out Retrofit APIs for Ktor’s HTTP client.
* Replace Moshi with Kotlinx Serialization for multiplatform support.

You can review [this commit](https://github.com/DenisBronx/RetrofitToKtorGuide/commit/deaa78d511a972a733df1268eecaabf711ecfea4) to see the complete migration from Retrofit to Ktor in the sample project.
## Step 3 (Bonus): Switch to NetMock Engine for Multiplatform
After completing the migration to Ktor, there’s one final step: migrating from NetMock Server (which is JVM-compatible) to NetMock Engine, the multiplatform flavor of NetMock.

By doing this, you maintain consistent testing practices while fully embracing the benefits of Kotlin Multiplatform, without needing to rely on JVM-specific solutions.

You can review [this commit](https://github.com/DenisBronx/RetrofitToKtorGuide/commit/368cbcdf1fc6c7f182403fa4504e9823f528af6b) to see the complete migration from netmock-server to netmock-engine.
