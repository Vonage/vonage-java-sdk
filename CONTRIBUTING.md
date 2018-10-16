# Contributing to the Nexmo Java Client Library

Thank you for your interest in contributing to the Nexmo Java Client Library. The following are a list of guidelines to follow when contributing. Please us your best judgement. If you feel that something in this document should be changed, feel free to propose changes in a pull request.

## I Have a Question About the Library

Please don't submit an issue to ask a question about the usage of the library. Issues should be submitted to report bugs or feature requests. General questions relating to [Nexmo] or the library should be directed to the following resources:

* [Nexmo Community Slack](https://developer.nexmo.com/community/slack)
* [Nexmo Support Knowledge Base](https://help.nexmo.com/hc/en-us)

These are much more active places to receive help with [Nexmo] or using the library. 

You also might find answers on [Nexmo Developer], our developer documentation portal.

## How Can I Contribute?

Contributions come in all shapes and sizes. Feel like a section might be missing? Open up a pull request for this document.

### Submitting a Bug Report

Unfortunately, bugs are just a part of developing software. This section will guide you through the process of submiting a bug report for the Nexmo Java Client Library. 

> For bug reports related directly to the Nexmo APIs, please see the following resource of [Current Known Problems](https://help.nexmo.com/hc/en-us/sections/115004147507-Current-Known-Problems) with instructions on how to submit a report.

Before submitting a report, check to see if it has already been reported in the [issues](https://github.com/Nexmo/nexmo-java/issues). If it hasn't been reported, open up a new issue. Some things to consider when opening an issue:

* Use a clear and descriptive title.
* Describe the steps needed to recreate the issue. Be as detailed as possible, something that seems trivial to you may not be the same for us. Try to include the following:
    * Which Java version are you using?
    * Which container are you using if this is a web application (Tomcat, Jboss, Jetty, etc..)?
    * Which version of the Nexmo Java Client Library are you using?
    * Was this something that happened recently without your code changing?
* Provide any related code examples. Try to include more than just the part of the code which involves the Nexmo Java Client Library as that will help us to diagnose other factors.
* If possible, propose a solution either in the issue or via a pull request.

### Suggesting Features

Is there a feature that's missing from the Nexmo Java Client Library? This section will guide you through the process of suggesting new features.

> For feature suggestions related directly to the Nexmo APIs, please see the following [Support Guide](https://help.nexmo.com/hc/en-us/articles/115008682828-Nexmo-Support-Guide) with instructions on how to submit a feature request.

Before submitting a feature, check to see if it has already been suggested in the [issues](https://github.com/Nexmo/nexmo-java/issues). If you find a closed issue with your suggested feature, and you feel strongly that it should be reopened, create a new issue and link to the old one. Some things to consider when opening an issue:

* Use a clear and descriptive title.
* Describe the feature in as much detail as possible.
* Provide justification for this feature. Why is it something that we should make a priority?
* Provide any related code examples, and explain what you're trying to accomplish.
* If possible, propose a solution either in the issue or via a pull request.

### Pull Requests

Want to get your hands a bit messy? Before you get started, here are some things you'll need to know.

Please complete the template in the pull request. There are three major tasks that should be completed:

* Add your name to `CONTRIBUTORS.md`.
* Add your changes to `CHANGELOG.md`. See information on how to [keep a changelog](https://keepachangelog.com/en/1.0.0/) to get an idea of what information is needed and appropriate.
* Update and add any unit tests to provide coverage for your change.

Once your pull request has been submitted, verify that it passes all test suites. Our current targeted version is Java 7, and using any features not supported in this version will cause the tests to fail.

Additionally, make sure that your code follows our styleguide listed below. The reviewer may ask you to make changes before your pull request can be accepted.

## Versioning

This project uses [Semantic Versioning](https://semver.org/) which means that changes to any `public` methods must be carefully considered. Setting a method to `public` is essentially giving anybody permission to use that method and any changes can force a major version increase.

## Styleguides

### Static Analysis

We use both Codacy and Codecov to perform static analysis on the code. Codacy has been setup to use a modified version of Checkstyle. You can download the configuration file here. (TODO: Add link)

### Java Standards

For the most part, we follow the default standards laid out in Checkstyle with some slight modifications. We also use most of the defaults that IntelliJ provides for code formatting. If you use IntelliJ, you can download the auto formatting configuration here. (TODO: Add link).

Here are the general best practices. When in doubt, follow the conventions set forth in the original file.

#### Indentation

We use 4 spaces for each level of indentation.

#### Spaces

In general, all parenthesis should have spaces before them except those in a method call or method declaration, `foo()` or `public static void foo()`. There should be no space around the arguments of a method.

Additionally, there should be spaces around all operators except unary operators, `foo++`, or double colon method references `Object::toString`. Spaces should also surround flow of control keywords such as `else`, `while`, `catch`, and `finally`.

#### Blank Lines

Keep maximum blank lines to two in a row, one is ideal. There should be a minimum of one blank line:

- After package statement
- Before imports
- After imports
- Between classes.
- Between methods.

#### Wrapping and Braces

Try to keep lines limited to around 120 characters. Braces should be placed at the end of the line, not on the next line.

The `extends`/`implements`/`throws` list doesn't need to be wrapped if it exceeds the line limit. Chained method calls and method arguments should either be chopped down if long, or wrapped. 

If chopping, place the parenthesis on their own lines:

```java
foo1(
    "bar1",
    "bar2",
    "bar3"
)
```

The same is true for chained method calls. The chained methods don't need to be aligned with each other:

```java
foobaz.bar1()
    .bar2()
    .bar3();
```

Long lines with many operators should be wrapped when long with the next line starting with an operator:

```java
condition1 || condition2 
    || condition3
```

The same is true for long ternary operations:

```java
(condition1) 
    ? doTrueBranch()
    : doFalseBranch()
```

#### Imports

Imports are organized in the following way:

- All imports that are not in the `javax.*` or `java.*` package.
- Single blank line
- All imports that are in the `javax.*` package. 
- All imports that are in the `java.*` package.
- Single blank line
- All static imports.

#### JavaDocs

JavaDocs should be used to provide additional value and clarity, not boilerplate documentation. It is recommended that you create JavaDocs for public methods. JavaDocs for non-public methods should only be created to add clarity. If a method's name clearly states its intention, there is no additional value in adding a JavaDoc.

If your proposed code changes don't include, what is believed to be sufficient, documentation you will be asked to add documentation.

#### Other Things

Try to keep methods short and extract logic out to private methods when appropriate. Remember, the use of `public` is giving permission to the user to use a method and can prove difficult to change in the future.

When encountering things not specifically outlined in this styleguide, use your best judgement. The goal of the styleguide is to create a consistent experience for all contributors.

[Nexmo]: https://nexmo.com
[Nexmo Developer]: https://developer.nexmo.com