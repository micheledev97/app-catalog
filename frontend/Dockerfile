# --- Build Angular ---
FROM node:20 AS build
WORKDIR /app
COPY package*.json ./
RUN npm ci
COPY . .
# forza l'output nella cartella /app/dist
RUN npm run build -- --configuration production --output-path=dist

# --- Runtime Nginx ---
FROM nginx:alpine
# SPA routing + proxy (se usi /api/* verso backend)
COPY nginx.conf /etc/nginx/conf.d/default.conf
COPY --from=build /app/dist/ /usr/share/nginx/html
EXPOSE 80
