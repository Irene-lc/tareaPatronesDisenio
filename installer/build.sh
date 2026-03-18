#!/bin/bash

# =============================================================================
# build.sh — Genera el instalador .pkg de ChatUPB para macOS
# Uso: ./build.sh
# =============================================================================

# ── CONFIGURACIÓN ─────────────────────────────────────────────────────────────
APP_NAME="ChatUPB"                          # Nombre visible de la app
APP_VERSION="1.0"                           # Versión del instalador
MAIN_CLASS="edu.upb.chatupb_v2.ChatUPB_V2" # Clase principal Java

PROJECT_DIR="$(cd "$(dirname "$0")/.." && pwd)"   # Raíz del proyecto (un nivel arriba de installer/)
JAR_NAME="ChatUPB_V2-jar-with-dependencies.jar"   # Nombre del fat JAR generado por Maven
ICON_FILE="$PROJECT_DIR/installer/icons/ChatUPB.icns"  # Ícono de la app
OUTPUT_DIR="$PROJECT_DIR/installer/output"         # Carpeta donde se guarda el .pkg final

JPACKAGE="/opt/homebrew/opt/openjdk@21/bin/jpackage"  # Ruta a jpackage
MVN="mvn"                                              # Comando Maven

# ── COLORES PARA LA TERMINAL ──────────────────────────────────────────────────
GREEN='\033[0;32m'
RED='\033[0;31m'
YELLOW='\033[1;33m'
NC='\033[0m' # Sin color

# =============================================================================
# PASO 1: Verificar que jpackage existe
# =============================================================================
echo ""
echo "🔍 Verificando herramientas..."

if [ ! -f "$JPACKAGE" ]; then
  echo -e "${RED} jpackage no encontrado en: $JPACKAGE${NC}"
  echo "   Asegurate de tener OpenJDK 21 instalado con Homebrew."
  exit 1
fi
echo -e "${GREEN} jpackage encontrado${NC}"

# =============================================================================
# PASO 2: Verificar que el JAR existe (compilado desde IntelliJ)
#
# ⚠️  IMPORTANTE: Este script NO compila el código fuente.
#     Antes de ejecutar este script debés hacer en IntelliJ:
#     Build → Build Artifacts → ChatUPB_V2-jar-with-dependencies → Build
#     (o usar Maven dentro de IntelliJ con: mvn clean package)
#
#     ¿Por qué? Lombok (que usa el proyecto) necesita el plugin de IntelliJ
#     para generar los métodos automáticamente. Maven desde la terminal no
#     lo procesa correctamente.
# =============================================================================
echo ""
echo " Verificando JAR compilado..."

JAR_PATH="$PROJECT_DIR/target/$JAR_NAME"
if [ ! -f "$JAR_PATH" ]; then
  echo -e "${RED} No se encontró el JAR en:${NC}"
  echo "   $JAR_PATH"
  echo ""
  echo -e "${YELLOW} Pasos para generarlo desde IntelliJ:${NC}"
  echo "   1. Abrí el proyecto en IntelliJ IDEA"
  echo "   2. En el panel Maven (derecha) → Lifecycle → package"
  echo "   3. Volvé a ejecutar este script"
  exit 1
fi
echo -e "${GREEN} JAR encontrado: $JAR_NAME${NC}"

# =============================================================================
# PASO 4: Limpiar output anterior y preparar carpeta de salida
# =============================================================================
echo ""
echo "  Preparando carpeta de salida..."
rm -rf "$OUTPUT_DIR"
mkdir -p "$OUTPUT_DIR"
echo -e "${GREEN} Carpeta lista: installer/output/${NC}"

# =============================================================================
# PASO 5: Generar el instalador .pkg con jpackage
#
# Qué hace jpackage:
#   --input          → carpeta donde está el JAR
#   --main-jar       → nombre del JAR principal
#   --main-class     → clase main de Java
#   --name           → nombre visible de la app
#   --app-version    → versión
#   --icon           → ícono .icns
#   --dest           → dónde guardar el .pkg
#   --type pkg       → formato instalador paso a paso
#   --java-options   → opciones JVM al arrancar la app
# =============================================================================
echo ""
echo "  Generando instalador .pkg con jpackage..."
echo -e "${YELLOW}   (Esto puede tardar 1-2 minutos...)${NC}"

"$JPACKAGE" \
  --input "$PROJECT_DIR/target" \
  --main-jar "$JAR_NAME" \
  --main-class "$MAIN_CLASS" \
  --name "$APP_NAME" \
  --app-version "$APP_VERSION" \
  --icon "$ICON_FILE" \
  --dest "$OUTPUT_DIR" \
  --type pkg \
  --mac-package-identifier "edu.upb.chatupb" \
  --java-options "-Xmx512m"

if [ $? -ne 0 ]; then
  echo -e "${RED} Error al generar el .pkg. Revisá los errores arriba.${NC}"
  exit 1
fi

# =============================================================================
# PASO 6: Confirmar resultado
# =============================================================================
PKG_FILE=$(ls "$OUTPUT_DIR"/*.pkg 2>/dev/null | head -n 1)
if [ -n "$PKG_FILE" ]; then
  echo ""
  echo -e "${GREEN} ¡Instalador creado exitosamente!${NC}"
  echo -e "    Ubicación: ${YELLOW}$PKG_FILE${NC}"
  echo ""
else
  echo -e "${RED} No se encontró el .pkg en la carpeta de salida.${NC}"
  exit 1
fi
