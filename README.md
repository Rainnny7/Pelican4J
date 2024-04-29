<img src="https://cdn.rainnny.club/X5auKsL4aMHL.png" alt="Pelican" width="132" align="right">

[![MIT License](https://img.shields.io/badge/License-MIT-limegreen.svg?style=plastic)](https://choosealicense.com/licenses/mit)
[![Maven Publish Workflow](https://git.rainnny.club/Rainnny/Pelican4J/actions/workflows/maven-publish.yml/badge.svg)](./actions?workflow=maven-publish.yml)
![Wakatime Hours](https://wakatime.rainnny.club/api/badge/Rainnny/interval:any/project:Pelican4J)
[![Discord](https://discord.com/api/guilds/827863713855176755/widget.png)](https://discord.gg/p9gzFE2bc6)

> [!IMPORTANT]
> This API is currently a WIP and is not yet complete.

# 🦅 Pelican4J
A Java API wrapper for the [Pelican](https://pelican.dev) and [Pterodactyl](https://pterodactyl.io) panel API.

---

# Table of Contents
- [Installation](#-installation)
  - [Maven](#maven)
  - [Gradle (Kotlin DSL)](#gradle-kotlin-dsl)
- [Getting Started](#-getting-started)
- [Panel Actions](#-panel-actions)
  - [Rate Limiting](#-rate-limiting)
  - [Error Handling](#-error-handling)

---

## 🔬 Installation
Before getting started, you must add the dependency to your project.
Below you can find the dependency for both [Maven](#maven) and [Gradle](#gradle-kotlin-dsl).

### Maven
```xml
<dependency>
  <groupId>me.braydon</groupId>
  <artifactId>Pelican4J</artifactId>
  <version>VERSION</version>
</dependency>
```

### Gradle (Kotlin DSL)
```kt
implementation("me.braydon:Pelican4J:VERSION")
```

## 🏃‍♂️ Getting Started
Below is an example on how to create a client for Pelican **(Ptero is also supported)**, and send an action to the panel.

```java
Pelican4J<PelicanPanelActions> client = Pelican4J.forPelican(ClientConfig.builder()
        .panelUrl("https://pelican.app")
        .apiKey("YOUR_API_KEY")
        .build()); // Create a new Pelican client

// Queuing an action - this will get the node with the ID of 1, and print it's name.
ApplicationNodeActions nodeActions = client.actions().application().nodes();
nodeActions.getDetails(1).queue(node -> {
    System.out.println("Name of node: " + node.getName());
});

// Instantly sending an action
Node node = nodeActions.getDetails(1).execute();
System.out.println("Name of node: " + node.getName());
```

## 🔨 Panel Actions
Every action that is executed on the panel will return a [PanelAction](./src/main/java/me/braydon/pelican/action/PanelAction.java), this action has several different functions:

- `queue(callback)` - This will allow you to queue an action, and await its response asynchronously.
- `execute()` - This will immediately execute the action synchronously, and return the result.

### ⌛ Rate Limiting
When an action is being queued, it will try to immediately execute the action, and if a rate limit is hit, that action will be queued to be re-tried later once the rate limit has been lifted. If the action is re-tried more than **25 Times**, it will respond with an error.

### ⚠️ Error Handling
When an error is raised by the panel API, an [PanelAPIException](./src/main/java/me/braydon/pelican/exception/PanelAPIException.java) exception will be thrown.

When queuing an action, errors are automatically handled and are printed to the terminal. To handle the error yourself, you can modify the callback function to contain both the response, and the exception:
```java
ApplicationNodeActions nodeActions = client.actions().application().nodes();
nodeActions.getDetails(1).queue((node, ex) -> {
    if (ex != null) {
        // Handle the error...
        return;
    }
    // ...
});
```

---

## YourKit
YourKit supports open source projects with innovative and intelligent tools for monitoring and profiling Java and .NET applications.
YourKit is the creator of [YourKit Java Profiler](https://www.yourkit.com/java/profiler), [YourKit .NET Profiler](https://www.yourkit.com/.net/profiler), and [YourKit YouMonitor](https://www.yourkit.com/youmonitor).

![Yourkit Logo](https://www.yourkit.com/images/yklogo.png)