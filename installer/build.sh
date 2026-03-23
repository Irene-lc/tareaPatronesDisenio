#!/bin/bash

# ── CONFIGURACIÓN ─────────────────────────────────────────────────────────────
APP_NAME="ChatUPB"                          # Nombre visible de la app
APP_VERSION="1.0"                           # Versión del instalador
MAIN_CLASS="edu.upb.chatupb_v2.ChatUPB_V2"  # Clase principal Java
BUNDLE_ID="edu.upb.chatupb"                 # Identificador del bundle

PROJECT_DIR="$(cd "$(dirname "$0")/.." && pwd)"   # Raíz del proyecto
JAR_NAME="ChatUPB_V2-jar-with-dependencies.jar"   # Nombre del fat JAR
ICON_FILE="$PROJECT_DIR/installer/icons/ChatUPB.icns"
OUTPUT_DIR="$PROJECT_DIR/installer/output"
RESOURCES_DIR="$PROJECT_DIR/installer/resources"

# Rutas de herramientas
JPACKAGE="/Library/Java/JavaVirtualMachines/temurin-21.jdk/Contents/Home/bin/jpackage"

# ── COLORES PARA LA TERMINAL ──────────────────────────────────────────────────
GREEN='\033[0;32m'
RED='\033[0;31m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
CYAN='\033[0;36m'
NC='\033[0m'

# ── FUNCIONES AUXILIARES ──────────────────────────────────────────────────────
print_header() {
    echo ""
    echo -e "${CYAN}  ChatUPB Installer Builder v${APP_VERSION}${NC}"
}

print_step() {
    echo ""
    echo -e "${YELLOW}▶ $1${NC}"
}

print_success() {
    echo -e "${GREEN}✓ $1${NC}"
}

print_error() {
    echo -e "${RED}✗ $1${NC}"
}
print_header

# PASO 1
print_step "Verificando herramientas..."

if [ ! -f "$JPACKAGE" ]; then
    print_error "jpackage no encontrado en: $JPACKAGE"
    echo "   Asegurate de tener OpenJDK 21 instalado."
    exit 1
fi
print_success "jpackage encontrado"

if ! command -v productbuild &> /dev/null; then
    print_error "productbuild no encontrado"
    echo "   Instalá Xcode Command Line Tools: xcode-select --install"
    exit 1
fi
print_success "productbuild encontrado"

if ! command -v pkgbuild &> /dev/null; then
    print_error "pkgbuild no encontrado"
    exit 1
fi
print_success "pkgbuild encontrado"

# PASO 2:
print_step "Verificando JAR compilado..."

JAR_PATH="$PROJECT_DIR/target/$JAR_NAME"
if [ ! -f "$JAR_PATH" ]; then
    print_error "No se encontró el JAR en: $JAR_PATH"
    echo ""
    echo -e "${YELLOW}Pasos para generarlo desde IntelliJ:${NC}"
    echo "   1. Abrí el proyecto en IntelliJ IDEA"
    echo "   2. En el panel Maven (derecha) → Lifecycle → package"
    echo "   3. Volvé a ejecutar este script"
    exit 1
fi
print_success "JAR encontrado: $JAR_NAME"

# PASO 3

print_step "Verificando recursos de personalización..."

REQUIRED_RESOURCES=("welcome.html" "readme.html")
MISSING_RESOURCES=()

for resource in "${REQUIRED_RESOURCES[@]}"; do
    if [ ! -f "$RESOURCES_DIR/$resource" ]; then
        MISSING_RESOURCES+=("$resource")
    fi
done

if [ ${#MISSING_RESOURCES[@]} -gt 0 ]; then
    print_error "Faltan archivos en installer/resources/:"
    for missing in "${MISSING_RESOURCES[@]}"; do
        echo "   - $missing"
    done
    echo ""
    echo -e "${YELLOW}Estos archivos definen las páginas del instalador.${NC}"
    exit 1
fi
print_success "Todos los recursos HTML encontrados"

if [ -f "$RESOURCES_DIR/background.png" ]; then
    print_success "Background personalizado encontrado"
    HAS_BACKGROUND=true
else
    echo -e "   ${YELLOW}(Sin background personalizado - usando default)${NC}"
    HAS_BACKGROUND=false
fi

#  PASO 4

print_step "Preparando carpetas de trabajo..."

rm -rf "$OUTPUT_DIR"
mkdir -p "$OUTPUT_DIR/app"
mkdir -p "$OUTPUT_DIR/scripts"
mkdir -p "$OUTPUT_DIR/pkg-root"

print_success "Carpetas listas"

# PASO 5

print_step "Generando aplicación .app con jpackage..."

"$JPACKAGE" \
    --input "$PROJECT_DIR/target" \
    --main-jar "$JAR_NAME" \
    --main-class "$MAIN_CLASS" \
    --name "$APP_NAME" \
    --app-version "$APP_VERSION" \
    --icon "$ICON_FILE" \
    --dest "$OUTPUT_DIR/app" \
    --type app-image \
    --mac-package-identifier "$BUNDLE_ID" \
    --java-options "-Xmx512m" \
    --java-options "-Dapple.awt.application.appearance=system"

if [ $? -ne 0 ]; then
    print_error "Error al generar la aplicación .app"
    exit 1
fi
print_success "Aplicación .app generada"

# PASO 6
print_step "Preparando estructura del paquete..."

mkdir -p "$OUTPUT_DIR/pkg-root/Applications"
cp -R "$OUTPUT_DIR/app/$APP_NAME.app" "$OUTPUT_DIR/pkg-root/Applications/"

print_success "Estructura preparada"

# PASO 7

print_step "Creando scripts de instalación..."

cat > "$OUTPUT_DIR/scripts/postinstall" << 'EOF'
#!/bin/bash
# Post-instalación de ChatUPB

# Dar permisos de ejecución
chmod -R 755 "/Applications/ChatUPB.app"

# Registrar en Launch Services
/System/Library/Frameworks/CoreServices.framework/Frameworks/LaunchServices.framework/Support/lsregister -f "/Applications/ChatUPB.app"

exit 0
EOF

chmod +x "$OUTPUT_DIR/scripts/postinstall"
print_success "Scripts creados"

# PASO 8
print_step "Creando componente del paquete..."

pkgbuild \
    --root "$OUTPUT_DIR/pkg-root" \
    --identifier "$BUNDLE_ID" \
    --version "$APP_VERSION" \
    --scripts "$OUTPUT_DIR/scripts" \
    --install-location "/" \
    "$OUTPUT_DIR/ChatUPB-component.pkg"

if [ $? -ne 0 ]; then
    print_error "Error al crear el componente .pkg"
    exit 1
fi
print_success "Componente creado"

# PASO 10
print_step "Generando distribution.xml..."

BACKGROUND_XML=""
if [ "$HAS_BACKGROUND" = true ]; then
    BACKGROUND_XML='<background file="background.png" alignment="bottomleft" scaling="proportional"/>'
fi

cat > "$OUTPUT_DIR/distribution.xml" << EOF
<?xml version="1.0" encoding="utf-8"?>
<installer-gui-script minSpecVersion="2">
    <title>ChatUPB</title>
    <organization>edu.upb</organization>
    <domains enable_localSystem="true"/>
    <options customize="never" require-scripts="true" rootVolumeOnly="true" hostArchitectures="x86_64,arm64"/>

    <!-- Páginas personalizadas del instalador -->
    <welcome file="welcome.html" mime-type="text/html"/>
    <readme file="readme.html" mime-type="text/html" title="Acerca de"/>

    <!-- Background personalizado -->
    ${BACKGROUND_XML}

    <!-- Definición de opciones de instalación -->
    <choices-outline>
        <line choice="default">
            <line choice="chatupb"/>
        </line>
    </choices-outline>

    <choice id="default"/>
    <choice id="chatupb" visible="false">
        <pkg-ref id="$BUNDLE_ID"/>
    </choice>

    <!-- Referencia al paquete componente -->
    <pkg-ref id="$BUNDLE_ID" version="$APP_VERSION" onConclusion="none">ChatUPB-component.pkg</pkg-ref>

</installer-gui-script>
EOF

print_success "distribution.xml generado"

# PASO 11
print_step "Generando instalador .pkg final con páginas personalizadas..."

productbuild \
    --distribution "$OUTPUT_DIR/distribution.xml" \
    --resources "$RESOURCES_DIR" \
    --package-path "$OUTPUT_DIR" \
    "$OUTPUT_DIR/ChatUPB-$APP_VERSION.pkg"

if [ $? -ne 0 ]; then
    print_error "Error al generar el instalador final"
    exit 1
fi

# PASO 12
print_step "Limpiando archivos temporales..."

rm -rf "$OUTPUT_DIR/app"
rm -rf "$OUTPUT_DIR/pkg-root"
rm -rf "$OUTPUT_DIR/scripts"
rm -f "$OUTPUT_DIR/ChatUPB-component.pkg"
rm -f "$OUTPUT_DIR/distribution.xml"

print_success "Limpieza completada"

# RESULTADO FINAL
PKG_FILE="$OUTPUT_DIR/ChatUPB-$APP_VERSION.pkg"
if [ -f "$PKG_FILE" ]; then
    echo ""
    echo -e "${BLUE}━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━${NC}"
    echo -e "${GREEN}  ¡INSTALADOR CREADO EXITOSAMENTE!${NC}"
    echo -e "${BLUE}━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━${NC}"
    echo ""
    echo -e "   Archivo: ${CYAN}$PKG_FILE${NC}"
    echo -e "   Tamaño:  ${CYAN}$(du -h "$PKG_FILE" | cut -f1)${NC}"
    echo ""
    echo -e "  ${YELLOW}Para probar el instalador:${NC}"
    echo -e "     open \"$PKG_FILE\""
    echo ""
else
    print_error "No se encontró el instalador en la carpeta de salida"
    exit 1
fi
