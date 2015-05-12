#!/bin/bash
#
# This script can be used for running word count and running median
#
# Author: Rajiv Porumalla

clear
echo "------------Starting Script------------"
echo
echo "------------Cleanup of files------------"
if [ -d "bin" ]; then
rm -r bin
fi
if [ -d "wc_output" ]; then
find wc_output -type f -name '*.txt' -delete
fi
echo
echo "------------Setting Proper Permissions for Programs------------"
chmod a+x src/WordCount.java
chmod a+x src/RunningMedian.java
echo
echo "------------Creating Classes Directory------------"
if [ ! -d "bin" ]; then
mkdir bin
fi
echo
echo "------------Creating Output Directory------------"
if [ ! -d "wc_output" ]; then
mkdir wc_output
fi
echo
echo "------------Compiling WordCount Program------------"
javac -d bin src/WordCount.java
if [ $? -eq 0 ]
then
echo "WordCount Compile Worked!"
chmod a+x bin/WordCount.class
fi
echo
echo "------------Compiling Running Median Program------------"
javac -d bin src/RunningMedian.java
if [ $? -eq 0 ]
then
echo "Running Median Compile Worked!"
chmod a+x bin/RunningMedian.class
fi
echo
cd bin
echo "------------Executing WordCount Program------------"
java WordCount ../wc_input ../wc_output/wc_result.txt
if [ $? -eq 0 ]
then
echo "WordCount Execution Worked!"
fi
echo
echo "------------Finished WordCount Program------------"
echo
echo "------------Executing Running Median Program------------"
java RunningMedian ../wc_input ../wc_output/med_result.txt
if [ $? -eq 0 ]
then
echo "RunningMedian Execution Worked!"
fi
echo
cd ..
echo "------------Cleanup of Class files------------"
if [ -d "bin" ]; then
rm -r bin
fi
echo
echo "------------Finished Running Median Program------------"
echo
echo "------------Finished Script------------"