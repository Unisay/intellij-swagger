# Intellij Swagger Plugin
Intellij Idea plugin that provides support for http://swagger.io

## Current state

Based on the sources from the Ansible/YAML plugin, that is in turn based on Neon plugin.
It contains a JFlex grammar (neon.flex) that is used to generate Lexer and a hand-written YAML Parser (NeonParser.java)
The Lexer is used for highlighting (via FlexAdapter from Intellij). 
The parser extends Intellij's PsiParser interface and is used for parsing and error highlighting. 

Unfortunately a limited subset of YAML functionality is implemented by both Lexer and Parser. This makes it unusable 
for the Swagger YAML as it requires more than supported currently: for example block-style literals containing Markdown
markup are not understood.

### YAML Schema: 

http://yaml.org/spec/1.2/spec.html#Schema

### Reference BNF for Haskell: 

http://hackage.haskell.org/package/YamlReference-0.9.3/src/Text/Yaml/Reference.bnf

### Grammar-Kit plugin for Intellij: 

### ANTLR support in jetbrains IDEs: 
- https://github.com/antlr/jetbrains
- https://devnet.jetbrains.com/message/5213839#5213839

### P.S.
http://leifw.wickland.net/2011/03/weekend-hacking-failure-net-parser-for.html