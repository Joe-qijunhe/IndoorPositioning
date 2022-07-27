# Indoor positioning data analysis

## Introduction

Indoor positioning is the process of estimating the location of objects in an indoor environment. It has many real-world application scenarios such as indoor navigation, monitoring and rescue operations. Existing localization systems like GPS do not work well in an indoor environment because obstacles like walls will block the signals. One popular solution is to use the Received Signal Strength (RSS) of Wi-Fi Access Points(APs) since RSS can reveal the information about the distance between objects and access points. This method has the potential to be ubiquitous because most of the buildings are covered by Wi-Fi and RSS values can be easily captured by devices within-built Wi-Fi chips. Therefore, this project aims to use the dual-band Wi-Fi signals
from multiple access points to determine the actual position of objects.

## About this repository

-   `IndoorPosition`: Java scripts that are used to generate distribution files, perform cross validation and so on.
-   `IndoorPositioningApp`: Android app that can collect data, test model and so on.
-   `NeuralNetwork`: python scripts that are used to build neural network model.
-   `TestAccuracy`: python scripts that are used to clean dataset, analyze accuracy and so on.