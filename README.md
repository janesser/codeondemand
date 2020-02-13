# The Problem

Once upon a time in a micro-service architecture.

API provides data. Easy.

API provides functionality. __Yeah, but do i really need to call for every invocation.__

## Considerations

Principle of location transparency: mostly location of API consumer and API producer can be neglected.

Principle of scalability: mostly the API producer does scale nicely along a varying number of API calls.

Principle of resilience: sometimes when an API call is not possible, the consumer will retry later.
__Sometimes API failure is cascading across multiple services.__ 

## Industry trend: cloud function
Deliver functionality on 1st class infrastructure.

This comes with a price. Find Pros and Cons in the web.

Random article: <https://www.freecodecamp.org/news/firebase-cloud-functions-the-great-the-meh-and-the-ugly-c4562c6dc65d/>

# Code-on-demand 

> REST allows client functionality to be extended by downloading and executing code in the form of applets or scripts.

From: 

Architectural Styles and the Design of Network-based Software Architectures,
Roy Thomas Fielding,
2000,
5.1.7 Code-On-Demand 
<https://www.ics.uci.edu/~fielding/pubs/dissertation/rest_arch_style.htm#sec_5_1_7>

## Schematic View

Service B <- Service A <- *A whole lot of clients*

### Idiomatic interaction Primitive
On every call of a client Service A needs to call Service B and integrates the results from Service B in his own processing.

### Idiomatic interaction Code-on-demand
Once in a while Service A downloads a script from Service B.
On every call of a client Service A executes the script from Service B.

The script is executed in a secured sandbox. The script may contain configuration and a function signature.

The contract is about:
+ where to get the script
+ how to use the script
  + function name
  + parameter annuity and types

Advantages of content-negotiation apply on script-delivery:
- versioning
- best-fit with client-capabilities

Not enough meta, huh?

# Polyglot run-times
<https://www.graalvm.org/docs/reference-manual/embed/>

<https://github.com/javadelight/delight-graaljs-sandbox>

# Testing & Debugging
Like with javascript the diversity of consumers can cause the producer a headache.
Easily the producer can test and debug cases, but hardly will cover all client-run-times.

On a rather uniform landscape the latter might be neglected.