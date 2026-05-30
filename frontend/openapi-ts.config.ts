import { defineConfig } from "@hey-api/openapi-ts";

export default defineConfig([
  {
    input: "http://localhost:8090/account-service/v3/api-docs",
    output: "src/api/generated/account-service",
    plugins: ["@hey-api/typescript", "@hey-api/sdk"],
  },
  {
    input: "http://localhost:8090/transaction-service/v3/api-docs",
    output: "src/api/generated/transaction-service",
    plugins: ["@hey-api/typescript", "@hey-api/sdk"],
  },
  {
    input: "http://localhost:8090/open-banking-service/v3/api-docs",
    output: "src/api/generated/open-banking-service",
    plugins: ["@hey-api/typescript", "@hey-api/sdk"],
  },
  {
    input: "http://localhost:8090/auth-service/v3/api-docs",
    output: "src/api/generated/auth-service",
    plugins: ["@hey-api/typescript", "@hey-api/sdk"],
  },
]);
