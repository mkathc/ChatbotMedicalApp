# Product Requirements Document (PRD)
## Asistente Conversacional para Aplicación Móvil de Salud

---

## 1. Problema

Los usuarios de aplicaciones de salud necesitan acceder rápidamente a
funcionalidades críticas —como triaje inicial de síntomas o gestión de citas—
en contextos de estrés, urgencia o conectividad limitada.

La navegación manual por múltiples pantallas incrementa la fricción,
dificulta el uso en situaciones sensibles y reduce la adopción de la aplicación,
especialmente en dispositivos móviles con conectividad inestable.

Se requiere una **interfaz inteligente**, integrada en una aplicación móvil,
que permita a los usuarios interactuar mediante **lenguaje natural** y que
orqueste las funcionalidades existentes de forma clara, segura y resiliente.

---

## 2. Objetivos

- Permitir a los usuarios acceder a funcionalidades clave de la aplicación
  mediante lenguaje natural, sin navegación manual compleja.
- Reducir la carga cognitiva del usuario en situaciones de estrés o urgencia.
- Permitir que funcionalidades críticas puedan operar bajo conectividad limitada.
- Diseñar una arquitectura escalable, resiliente y preparada para producción.
- Proteger datos sensibles de salud (PHI) desde el diseño.
- Facilitar la evolución futura del sistema manteniendo baja complejidad cognitiva.

---

## 3. No Objetivos

- Proveer diagnósticos médicos definitivos o clínicamente validados.
- Reemplazar la atención médica profesional.
- Implementar lógica médica compleja o regulatoria.
- Optimizar o entrenar modelos de lenguaje.
- Cubrir exhaustivamente todos los casos médicos posibles.

---

## 4. Usuarios

- **Paciente / Usuario final**
    - Usuario de la aplicación móvil de salud.
    - Puede encontrarse en situaciones de estrés, urgencia o conectividad limitada.

- **Sistemas de Salud (externos)**
    - Sistemas de triaje, agendamiento, historial médico o recordatorios.
    - Representados en este reto mediante implementaciones simplificadas o mocks.

---

## 5. Modelo de Interacción (Hybrid Assistant)

El sistema adopta un **Hybrid Assistant**, donde:

- El lenguaje natural se utiliza como interfaz principal para:
    - detección de intención,
    - captura progresiva de información,
    - confirmación de acciones.
- Los flujos críticos de negocio se ejecutan de forma **determinística**,
  mediante lógica nativa y validable.
- La UI nativa actúa como **apoyo** para:
    - selección de opciones complejas (calendarios, listas),
    - confirmaciones explícitas,
    - fallback ante ambigüedad conversacional.

El asistente conversacional **orquesta** los flujos, pero no toma decisiones
médicas autónomas.

---

## 6. Capacidades Funcionales

### 6.1 Triaje de Síntomas
**Nivel de implementación: Alta (PoC completa)**

**Descripción**  
El usuario describe síntomas en lenguaje natural.
El sistema evalúa la severidad mediante reglas simplificadas y sugiere
la siguiente acción apropiada:

- Emergencia
- Agendar cita
- Autocuidado

**Requisitos**
- Detección de intención y captura progresiva de síntomas.
- Evaluación determinística basada en reglas simples.
- Soporte **offline** para un conjunto mínimo de reglas de triaje.
- Degradación elegante cuando la red o la IA no están disponibles.
- Mensajes claros que indiquen limitaciones del sistema.

---

### 6.2 Agendamiento de Citas
**Nivel de implementación: Alta (PoC completa)**

**Descripción**  
El usuario puede buscar disponibilidad médica, reservar, modificar o cancelar
citas **mediante conversación**.

El asistente:
- interpreta la intención,
- captura parámetros (especialidad, fecha, sede, médico),
- presenta opciones disponibles,
- solicita confirmación antes de ejecutar la operación.

La UI nativa se utiliza como apoyo para la selección de horarios
(calendario o lista) y como fallback ante ambigüedad.

**Requisitos**
- Flujos conversacionales para creación, modificación y cancelación.
- Integración con un servicio de agendamiento simulado (stub o fake).
- Confirmación explícita antes de ejecutar operaciones.
- Manejo de errores de red, reintentos y mensajes de degradación.

---

### 6.3 Consulta de Información Médica
**Nivel de implementación: Baja (stub/fake)**

**Descripción**  
El usuario puede consultar información médica básica como:
- historial de citas,
- resultados de exámenes,
- recetas activas.

**Requisitos**
- Acceso a datos simulados o en memoria.
- Flujo conversacional simple.
- No se requiere soporte offline completo.

---

### 6.4 Gestión de Recordatorios
**Nivel de implementación: Baja (stub/fake)**

**Descripción**  
Permite crear y consultar recordatorios de medicamentos o citas próximas.

**Requisitos**
- Creación de recordatorios locales en el dispositivo.
- Sincronización opcional cuando exista conectividad.
- Integración básica con el asistente conversacional.

---

## 7. Requisitos No Funcionales

### 7.1 Escalabilidad horizontal
- Soporte para miles de conversaciones concurrentes.
- Arquitectura preparada para escalar de miles a millones de usuarios
  sin rediseño del sistema core.

### 7.2 Resiliencia y degradación
- Manejo explícito de fallos de red, servicios externos o IA.
- Degradación elegante hacia reglas locales o flujos guiados.

### 7.3 Performance bajo restricciones
- Optimización para dispositivos móviles de gama baja.
- Reducción de latencia percibida y consumo de batería.
- Soporte para conectividad 3G o intermitente.

### 7.4 Privacidad by design
- Protección de datos de salud (PHI) desde el diseño.
- Comunicación segura y almacenamiento protegido.
- Principio de mínimo acceso a datos.

### 7.5 Baja complejidad cognitiva
- Módulos profundos con interfaces simples.
- Separación clara de responsabilidades.
- Ocultamiento de complejidad interna (information hiding).

### 7.6 Visión multiplataforma (Android / iOS)
- Soporte para aplicaciones Android e iOS.
- Reutilización de lógica core entre plataformas sin duplicación innecesaria.
- Consistencia funcional entre plataformas.

---

## 8. Restricciones y Supuestos

- El sistema es **mobile-first**: diseñado prioritariamente para entornos móviles.
- Solo el triaje inicial requiere soporte offline explícito.
- Las integraciones médicas reales se simulan mediante stubs o fakes.
- El tiempo de implementación está orientado a demostración arquitectónica.

---

## 9. Open Questions

- ¿Qué reglas mínimas de triaje deben residir en el dispositivo?
- ¿Dónde se mantiene el estado conversacional: local, backend o híbrido?
- ¿Cómo versionar y actualizar reglas de triaje sin forzar releases frecuentes?
- ¿Qué métricas mínimas de uso, fallos y degradación deberían instrumentarse?
