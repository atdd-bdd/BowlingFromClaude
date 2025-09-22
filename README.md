# Coding with Claude

This project started with a version of bowling written by Claude.   The input was a Gherkin extended file, shown in [BowlingFromClaude/bowling.feature at main · atdd-bdd/BowlingFromClaude · GitHub](https://github.com/atdd-bdd/BowlingFromClaude/blob/main/bowling.feature) .  The Gherkin file includes scenarios that describe domain terms and business rules, as well as the usual Given/When/Then flows.

The  syntax of a Gherkin extended file is described in detail at [GitHub - atdd-bdd/GherkinExecutorBase: This is the base for Gherkin Executor containing Documentation and Examples](https://github.com/atdd-bdd/GherkinExecutorBase). A key aspect is that Data elements specify the data types/domain types of every field in step tables.  

That additional information allows Claude to process the file with less ambiquity.   Claude did not provide a main program that asked for the input of each roll.  When it was informed about that, it added one.  It worked fine the first time.   

Claude has been used to code other feature extended files:

1. One is from the examples for the Gherkin Executor - an ID domain term and a filter (filter.featurex).   This one worked the first time it was translated.  

2. An example of a Dollar domain term with extensive input validation and alternative output formats.   This one required asking Claude to fix errors in the tests.   These were due to the wording of the error message - using a generic one when a specific one was asked for.   Feeding in the errors let it correct them.    The one other issue was that some of the error messages were created for just the specific example in the feature file.  When Claude was informed of that, it corrected the code.     The kata associated with this is at https://kenpugh.com/Articles/A%20Dollar%20Kata.pdf

3. A third example (RDDExample) comes from a Requirements Driven Development / Test Driven Development article.   https://kenpugh.com/Articles/Requirements-Driven-Development-Test-Driven-Development.pdf    This is from Kent's Beck book on Test Driven Development.    Claude had a couple of issues which it fixed when it was informed of them.    In the Money class, the attributes are set as `final`.  However, it attempted to set the value in various places in the constructor.   It corrected this using a temporary variable.   The other issue was comparing amounts in the tests.   When it corrected the issue, it said:* This is the correct way to compare BigDecimal values when you care about numerical equality but not the scale*.

4. Claude was asked to use value objects from Instrument and Shares for the RDDExample.  This is RDDExampleUpdate.   It did so.  It also showed how to have this suggestion used for future code. 

5. Computing with fractions (from J. B. Rainsberger) is shown.  The unit tests ran the first time with the exception that the code produced a result of 3/3, rather than 1.  After being informed of this, Claude updated the necessary file and all tests passed.   




