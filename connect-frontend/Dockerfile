# Stage 1: Build the Angular application
FROM node:22 AS build

WORKDIR /app

COPY package*.json ./

# Install dependencies including Angular CLI globally
RUN npm ci
RUN npm install -g @angular/cli

# Copy the rest of the application
COPY . .

# Debug - list files to verify everything was copied correctly
RUN ls -la

# Build the application with SSR enabled
RUN npm run build

# Stage 2: Serve the application
FROM node:22-slim

WORKDIR /app

# Copy the build output from the build stage
COPY --from=build /app/dist /app/dist
COPY --from=build /app/package*.json /app/

# Install only production dependencies
RUN npm ci --omit=dev

# Expose the port the app runs on
EXPOSE 4200

# Command to run the application
CMD ["node", "dist/ksoft/server/server.mjs"]
