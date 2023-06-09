#!/bin/bash
#./clear_output.sh
#jadx -d ./output/source /Users/jdanceze/Desktop/hub/cg_parser/output/com.asiandate.apk
#jadx -d ./output/source <apklocation>
#python3 setting.py ./com.autostart.apk/com.autostart.txt ./com.autostart.apk/_workspace_finance_10_com.autostart.apk/covids ./com.autostart.apk/_workspace_finance_10_com.autostart.apk/coverage.dat.2023-05-08-13-53-00
#python3 setting.py <soot cg> <covid file> <dynamic cg>
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
./fill_default_values_mac.sh ./output/final combinationWithExtraFeatures_label.csv
./fill_default_values_mac.sh ./output/final combinationWithExtraFeatures.csv