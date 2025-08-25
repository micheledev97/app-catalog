#!/usr/bin/env bash
set -euo pipefail

ROOT_DIR="$(cd "$(dirname "$0")" && pwd)"

echo "==> Frontend: lint"
(cd "$ROOT_DIR/frontend" && npm ci && npm run lint || true)

echo "==> Frontend: test"
(cd "$ROOT_DIR/frontend" && npm run test || true)

echo "==> Frontend: build"
(cd "$ROOT_DIR/frontend" && npm run build)

echo "==> Backend: build"
(cd "$ROOT_DIR/backend" && mvn -q -DskipTests package)

echo "==> Docker build & compose up"
docker compose build
docker compose up -d

echo "==> Done!"
