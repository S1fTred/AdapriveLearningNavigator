# ALN Postman Manual Checks

## Before import
1. Start the application.
2. For an existing file-based H2 database with `spring.sql.init.mode=never`, run:
   - [auth_password_hash_migration.sql](/C:/Users/user/VSProjects/AdapriveLearningNavigator/docs/sql/auth_password_hash_migration.sql)
3. Seed the knowledge base required for AI plan generation:
   - [manual_api_seed.sql](/C:/Users/user/VSProjects/AdapriveLearningNavigator/docs/postman/manual_api_seed.sql)
4. Start Ollama and ensure `qwen2.5:7b` is available if you want live AI checks.

## Import
1. Import [AdapriveLearningNavigator.local.postman_environment.json](/C:/Users/user/VSProjects/AdapriveLearningNavigator/docs/postman/AdapriveLearningNavigator.local.postman_environment.json)
2. Import [AdapriveLearningNavigator.postman_collection.json](/C:/Users/user/VSProjects/AdapriveLearningNavigator/docs/postman/AdapriveLearningNavigator.postman_collection.json)
3. Select the `ALN Local` environment.

## Recommended run order
1. `Auth / Register`
2. `Auth / Login`
3. `Auth / Refresh`
4. `Plan / Security & Validation`
5. `Plan / Live AI`

## Notes
- `registerEmail` is auto-generated once by the collection pre-request script if you leave it empty.
- Successful `register`, `login`, and `refresh` requests automatically store `accessToken` and `refreshToken`.
- Successful `generate-with-ai` responses automatically store `planId`.
- AI plan generation is nondeterministic because it depends on the live LLM response.
