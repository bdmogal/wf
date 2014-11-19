Workflow Generator
==================

[Bhooshan Mogal](mailto:bhooshan.mogal@gmail.com)

A tool that provides an API (described below) to read job metadata and suggest a workflow composed of stages of execution.
Each stage contains jobs all of whose dependencies have been satisfied in prior stages.
Currently used in a standalone java application so it is easier to test. But a REST API could easily be added
using most of the same code base.

The API
=======
    WorkflowManager.execute(String inputType, String inputSrc)
    inputType: the type of input - could be 'jsonfile', 'interactive' or 'properties'
    inputSource: the source of data for the input used for input types jsonfile (points to the json file) and
                properties (points to the properties file)

A sample application using this API is shown in WorkflowRunner.java.

Input Formats
=============
The best way to specify input for such a tool would be a web UI. However, for this assignment, the following modes are supported:
1. jsonfile - as a json file
2. interactive - interactive command line
3. properties - as a properties file

Build
=====
Using gradle
------------

    cd [workflow_project_directory]
    chmod +x gradlew
    ./gradlew clean build

Using javac
-----------

    cd [workflow_project_directory]
    javac -cp libs/*:src/main/java/workflow/* -d /tmp/out src/main/java/workflow/*.java

Run
===
If built using gradle
---------------------
    java -cp build/libs/workflow-1.0.jar workflow.WorkflowRunner [jsonfile|interactive|properties] [Optional file]
    [Optional file] is the absolute path to the file containing json data for input type 'jsonfile'
    or to the file containing properties for input type 'properties'

If built using javac
--------------------
    java -cp /tmp/out:libs/* workflow.WorkflowRunner [jsonfile|interactive|properties] [Optional file]
    [Optional file] is the absolute path to the file containing json data for input type 'jsonfile'
    or to the file containing properties for input type 'properties'


Examples
========
Using a JSON File as Input
--------------------------
    java -cp build/libs/workflow-1.0.jar workflow.WorkflowRunner jsonfile /path/to/workflow.json

Using Interactive Input
-----------------------
    java -cp /tmp/out:libs/* workflow.WorkflowRunner interactive

Using a Properties File as Input
--------------------------------
    java -cp build/libs/workflow-1.0.jar properties /path/to/workflow.properties
