#!/bin/bash

input_file="$1"

# Check if the input file is provided
if [ -z "$input_file" ]; then
  echo "Please provide an input file."
  exit 1
fi

# Check if the input file exists
if [ ! -f "$input_file" ]; then
  echo "Input file '$input_file' not found."
  exit 1
fi

while IFS= read -r line || [[ -n "$line" ]]; do
    echo "Read: $line"
    rm ./temp.txt
    echo "$line" >> temp.txt

    apk_location=$(python3 apk_location.py ./temp.txt)
    echo "APK location: $apk_location"
    echo $apk_location >> apk_location.txt
    rm ./temp.txt
    
done < "$input_file"
