#!/bin/bash
package=$(python3 package_name.py)
mkdir ./data/$package
echo "apk name: " $package
./clear_output.sh
apk_location=$(python3 apk_location.py)
echo "apk location: " $apk_location
jadx -d ./output/source $apk_location
soot_cg=$(python3 soot_location.py)/$package.txt
echo "soot location: " $soot_cg
ella_location=$(python3 ella_location.py)
echo "ella location: " $ella_location
current_dir=$(pwd)
cd $ella_location
cat coverage.dat.* > coverage.dat.2024-06-11-18-04-31
cd $current_dir
covid_location=$(python3 covid_location.py)
echo "covid location: " $covid_location
cov_locaution=$(python3 cov_location.py)
echo "cov location: " $cov_locaution
python3 setting.py $soot_cg $covid_location $cov_locaution
echo "start prase_cg_to_csv.py"
python3 prase_cg_to_csv.py
echo "start refine_csv.py"
python3 refine_csv.py
echo "start format_covid.py"
python3 format_covid.py
echo "start gen_coverage_sig_set.py"
python3 gen_coverage_sig_set.py
echo "start label_csv.py"
python3 label_csv.py
echo "start remove_std_lib_edges.py"
python3 remove_std_lib_edges.py
echo "start generate_extra_information_from_dataset.py"
python3 generate_extra_information_from_dataset.py ./output/ True
echo "start refine_cg_label.py"
python3 refine_cg_label.py
echo "start rename_header.py"
python3 rename_header.py
echo "set default values"
./fill_default_values.sh ./output/final combinationWithExtraFeatures_label.csv
./fill_default_values.sh ./output/final combinationWithExtraFeatures.csv
java -jar ./java/MethodSourceCodeRetriever.jar ./output/methods_reference.txt ./output/source ./output/final/sig_to_func.csv
mv ./output ./data/$package/output