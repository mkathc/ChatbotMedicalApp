# Health Assistant Conversacional – Arquitectura (PoC Mobile)

## Resumen Ejecutivo

Este repositorio presenta el diseño e implementación de la **arquitectura de un asistente conversacional integrado en una aplicación móvil de Salud**, donde el chatbot actúa como **interfaz inteligente** para permitir a los usuarios acceder a las funcionalidades de la app mediante **lenguaje natural**, sin necesidad de navegar manualmente por múltiples pantallas.

El foco del reto no es la implementación funcional completa, sino **demostrar pensamiento arquitectónico**, priorizando:
- performance en dispositivos móviles,
- escalabilidad,
- resiliencia,
- privacidad de datos de salud (PHI),
- y una visión clara multiplataforma.

La solución se presenta como una **PoC Android**, con **visión explícita para iOS y producción**.

---

## Visión Arquitectónica

La arquitectura adopta un enfoque de **Hybrid Assistant**:

- El **chatbot no es solo IA**, sino un **orquestador de capacidades**.
- Algunas decisiones se resuelven mediante **conversación natural**.
- Otras acciones críticas se ejecutan mediante **UI determinística** (pantallas o diálogos de confirmación).

### Principios clave
- **Mobile-first**: la experiencia está optimizada para mobile (latencia, batería, UX).
- **Offline parcial**: el triaje básico funciona sin conexión.
- **Contract-first**: la lógica de negocio se modela como *Commands* y *Tools*.
- **Privacidad by design**: el cliente móvil no expone PHI a proveedores de IA.
- **Baja complejidad cognitiva**: módulos profundos con interfaces simples.

---

## Abordaje del reto

### 1. Performance bajo restricciones
- Triaje offline mediante reglas determinísticas.
- Persistencia local cifrada del estado conversacional (TTL 24h).
- Reducción de dependencias en tiempo real con backend.
- Uso de UI determinística para flujos críticos (agendamiento / confirmación).

### 2. Escalabilidad horizontal
- El asistente delega la orquestación compleja al **BFF (Backend for Frontend)**.
- El backend expone herramientas desacopladas (Tools) que pueden escalar independientemente.
- La arquitectura soporta crecer de miles a millones de usuarios sin rediseño estructural.

### 3. Resiliencia y degradación elegante
- Fallback a capacidades locales cuando la red o la IA no están disponibles.
- Separación clara entre conversación, dominio y servicios externos.
- Posibilidad de circuit breakers, retries y colas offline (definido a nivel arquitectónico).

### 4. Multiplataforma
- La UI se mantiene **100% nativa** (Compose / SwiftUI).
- La arquitectura está diseñada para reutilizar lógica core (visión KMP).
- En producción, la IA y las políticas viven en el backend, evitando duplicación innecesaria.

---

## Alcance de la PoC

### Implementación más detallada
- **Triaje de síntomas** (offline, reglas básicas).
- **Agendamiento y cancelación de citas** mediante conversación + confirmación explícita.

### Implementación simplificada
- Consulta de información médica.
- Gestión de recordatorios (locales en PoC).

Este enfoque permite demostrar las decisiones arquitectónicas clave sin sobre–implementar el dominio.

---

## Estructura del repositorio

La documentación arquitectónica se encuentra organizada de la siguiente manera:

```
/docs
├── prd.md # Product Requirements Document
├── adr/
│ ├── ADR-001.md # Multiplataforma / estrategia mobile
│ ├── ADR-002.md # Estado conversacional
│ ├── ADR-003.md # Privacidad y BFF
│ ├── ADR-004.md # Tools / Commands (contract-first)
│ ├── ADR-005.md # Triaje offline
│ └── ADR-006.md # Resiliencia y degradación
└── c4/
├── c4-context.puml # C4 Level 1 – Context
├── c4-container.puml # C4 Level 2 – Container
└── c4-component.puml # C4 Level 3 – Component
```

La PoC Android se encuentra en el módulo `app/` y refleja directamente los conceptos definidos en los ADR y diagramas C4.

Para un detalle técnico completo ver docs/architecture-overview.md

---

## Nota final

Esta solución prioriza **claridad arquitectónica y capacidad de evolución**, dejando explícitas las decisiones tomadas y los trade-offs asumidos.  
La arquitectura está preparada para producción, aunque la implementación presentada es una PoC enfocada en evaluación técnica.
