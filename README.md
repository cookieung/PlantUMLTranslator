# PlantUMLTranslator

This is a project for translation plantUML to CSP. It is a part of implementation of the internship project at Okayama Prefectural.


How to use it:
```
At this time, the program has just been developing so run file src/Controller/PlantReader.java or dist/PlantUMLTranstalor.jar

1.Browse your state diagrams and your sequence diagrams that are in the format of Plant UML file, then click read every file. All of the source code will be show in the left box.
2.Click convert to CSP button if you browse it complete. Then, the right box will show the result in the CSP format.
3.Name the output name and choose "Save CSP file" for save the result in to a CSP file.
```

What it support now?

|                 | STATE DIAGRAM | SEQUENCE DIAGRAM |     MULTIPLE     |     COMPLEX      |
|-----------------|:-------------:|:----------------:|:----------------:|:----------------:|
| STATE DIAGRAM   |       /       |        /         |        -         |        -         |
| SQ with ALT/OPT |       /       |        /         |        /         |     in process   |
| SQ with LOOP    |       /       |        /         |        /         |     in process   |


updated : 8/1/2017
Salilthip Phuklang