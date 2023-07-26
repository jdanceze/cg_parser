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

    package=$(python3 package_name.py ./temp.txt)
    echo "Package name: $package"
    echo $package >> package_name.txt
    rm ./temp.txt
    
done < "$input_file"
