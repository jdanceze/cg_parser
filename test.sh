#!/bin/bash

input_file="/Users/jdanceze/Desktop/hub/cg_parser/demo/cov_de.txt"

# Check if the input file exists
if [ ! -f "$input_file" ]; then
  echo "Input file '$input_file' not found."
  exit 1
fi

# Read each line from the input file
while IFS= read -r line || [[ -n "$line" ]]; do
  # Create a new text file for each line
  #output_file="${line//\//-}.txt"
  echo $line
  # Write the line to the new file
  #echo "$line" > "$output_file"
  
  #echo "Created file: $output_file"
done < "$input_file"