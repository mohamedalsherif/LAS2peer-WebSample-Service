LAS2peer-WebSample-Service
==========================

This is a sample Service that uses the LAS Ajax Client to demonstrate Service methods


This tutorial serves as a very basic tutorial to the LAS2Peer Project. This service runs on a local LAS2Peer node and does not require any additional software to be installed.

To run this tutorial you need to download this project and build the "build.xml" file using ant.  After successfully running the build file, the Web Sample Service will be compiled in a jar, stored in the "lib/" folder.

To open a LAS2Peer Node that acts as a server that runs the Web Sample Service, you need to run the only script in "scripts/" folder.  After running the script you would have a running node on your localhost on port 8080.

Open a web browser and open "WebContent/index.html" to test your service.  Be aware that you may need to disable the same origin policy on the web browser you are using in order to use the frontend.

After successfully viewing the "index.html" and logging in, you can test the service methods that are in the Web Sample Service. In order to add or change methods in the service you need to do the following steps:

a) After changing the Service code, build using ant.

b) Close the node that's running on the localhost:8080.

c) Reopen the node again (by running the only script in "scripts/").

d) Refresh the browser and check the changes you have done.
