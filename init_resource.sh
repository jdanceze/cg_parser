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
    mkdir -p "./data/$package"
    echo "APK name: $package"
    
    ./clear_output.sh
    
    apk_location=$(python3 apk_location.py ./temp.txt)
    echo "APK location: $apk_location"
    jadx -d ./output/source "$apk_location"
    
    soot_cg=$(python3 soot_location.py ./temp.txt)/$package.txt
    echo "Soot location: $soot_cg"
    
    ella_location=$(python3 ella_location.py ./temp.txt)
    echo "Ella location: $ella_location"
    
    current_dir=$(pwd)
    cd "$ella_location"
    cat coverage.dat.* > coverage.dat.2024-06-11-18-04-31
    cd "$current_dir"
    
    covid_location=$(python3 covid_location.py ./temp.txt)
    echo "Covid location: $covid_location"
    
    cov_location=$(python3 cov_location.py ./temp.txt)
    echo "Cov location: $cov_location"
    
    python3 setting.py "$soot_cg" "$covid_location" "$cov_location"
    
    echo "Start prase_cg_to_csv.py"
    python3 prase_cg_to_csv.py
    
    echo "Start refine_csv.py"
    python3 refine_csv.py
    
    echo "Start format_covid.py"
    python3 format_covid.py
    
    echo "Start gen_coverage_sig_set.py"
    python3 gen_coverage_sig_set.py
    
    echo "Start label_csv.py"
    python3 label_csv.py
    
    echo "Start remove_std_lib_edges.py"
    python3 remove_std_lib_edges.py
    
    echo "Start generate_extra_information_from_dataset.py"
    python3 generate_extra_information_from_dataset.py ./output/ True
    
    echo "Start refine_cg_label.py"
    python3 refine_cg_label.py
    
    echo "Start rename_header.py"
    python3 rename_header.py
    
    echo "Set default values"
    ./fill_default_values.sh ./output/final combinationWithExtraFeatures_label.csv
    ./fill_default_values.sh ./output/final combinationWithExtraFeatures.csv
    
    java -jar ./java/MethodSourceCodeRetriever.jar ./output/methods_reference.txt ./output/source ./output/final/sig_to_func.csv
    
    mv ./output "./data/$package/output"
    rm ./temp.txt
    
done < "$input_file"
