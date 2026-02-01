# Asistente Conversacional para Aplicación Móvil de Salud

## 1. Objetivo del sistema
Diseñar la arquitectura de un asistente conversacional integrado en una aplicación móvil de salud que permita a los usuarios interactuar mediante lenguaje natural para:

- Triaje de síntomas
- Agendamiento y gestión de citas
- Consulta de información médica
- Gestión de recordatorios

El asistente actúa como una **interfaz inteligente**, reduciendo la necesidad de navegar manualmente por múltiples pantallas, manteniendo seguridad, resiliencia y buen rendimiento en dispositivos móviles.

---

## 2. Alcance de la solución
- **Android**: Proof of Concept (PoC)
- **iOS**: Visión arquitectónica
- **Backend**: Orquestación y políticas (BFF)
- **IA**: Integración desacoplada mediante adapters

Para el reto:
- 2 capacidades implementadas con mayor profundidad (Triaje, Agendamiento)
- 2 capacidades implementadas de forma simplificada (Historial, Recordatorios)

---

## 3. Enfoque arquitectónico

### 3.1 Hybrid Assistant (Conversacional + UI determinística)
El asistente combina:
- Conversación en lenguaje natural
- Pantallas de confirmación visual para acciones críticas (crear/modificar citas)
- Confirmaciones simples en chat para acciones no críticas (cancelar)

Este enfoque reduce errores, mejora la confianza del usuario y mantiene control clínico.

---

### 3.2 Mobile-first con capacidades offline
El sistema está diseñado priorizando:
- conectividad inestable
- dispositivos de gama baja
- bajo consumo de batería

El **triaje inicial** puede operar offline mediante reglas mínimas locales, permitiendo una clasificación básica aun sin red.

---

## 4. Decisiones Arquitectónicas (ADR)
Las principales decisiones están documentadas en los ADR:

- **ADR-001**: Hybrid Assistant (chat + UI)
- **ADR-002**: Estado conversacional local con TTL de 24h
- **ADR-003**: Backend (BFF) como punto único de integración IA y políticas PHI
- **ADR-004**: Orquestación basada en Tools/Commands
- **ADR-005**: Triaje offline mínimo
- **ADR-006**: Resiliencia y degradación controlada

---

## 5. Modelo C4

### 5.1 C4 – Context
El sistema está compuesto por:
- Usuario (Paciente)
- App móvil de salud
- BFF / Assistant API
- Proveedor de IA (externo)
- Servicios médicos (citas, historial)
- Servicios de notificaciones (APNs / FCM – visión producción)

El usuario interactúa únicamente con la app móvil.  
La app nunca se comunica directamente con el proveedor de IA.

---

### 5.2 C4 – Container
Containers principales:
- **App Móvil (Android PoC / iOS visión)**  
  UI conversacional, estado local, triaje offline, recordatorios locales.
- **Local Encrypted Store**  
  Estado conversacional (TTL 24h), reglas de triaje, recordatorios.
- **BFF / Assistant API**  
  Orquestación, políticas PHI, integración IA, tool-calling.
- **Servicios de dominio (stubs en PoC)**  
  Citas e historial médico.
- **Proveedor IA (externo)**  
  Accedido únicamente por el BFF.
- **Push Provider (APNs / FCM)**  
  Notificaciones (visión producción).

---

### 5.3 C4 – Component
#### App Móvil
- Chat UI
- Conversation Controller
- Local Intent Router
- Offline Triage Engine
- Conversation State Store
- Local Reminders Manager

#### BFF / Assistant API
- API Controller
- Intent Interpreter (híbrido: reglas + LLM)
- Conversation Orchestrator
- Tool Dispatcher
- LLM Provider Port + Adapters
- PHI Policy Filter

---

## 6. Privacidad y seguridad (Privacy by Design)
- El móvil no integra SDKs de IA.
- El BFF aplica minimización y redacción de PHI.
- Logs y métricas sin datos sensibles.
- Estado local cifrado en dispositivo.

---

## 7. Resiliencia y degradación
- Timeouts explícitos
- Retries con backoff
- Circuit breakers por dependencia
- Fallback a capacidades offline
- Mensajes claros al usuario cuando una función está degradada

---

## 8. Evolución a producción
- Sustituir stubs por servicios reales
- Sincronización de recordatorios y notificaciones push
- Persistencia distribuida del estado conversacional
- Escalado horizontal del BFF
- Observabilidad completa sin PHI

---

## 9. Conclusión
La arquitectura propuesta prioriza:
- experiencia móvil
- seguridad de datos de salud
- resiliencia ante fallos
- baja complejidad cognitiva
- capacidad de evolución sin rediseños mayores

El diseño es viable como PoC y escalable a un entorno productivo.
