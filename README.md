# cg_paser

## Step
1. Config the setting: `python3 setting.py <path to soot static callgraph> <path to covids file> <path to coverage.dat file>`
2. Clear the previous output: `./clear_output.sh` (also need to run on first time)
3. Start the parser: `./run_cg_parser.sh`

## Output
Output files are stored in `output` folder.
- combinationWithExtraFeatures.csv: a final static callgraph use for training (include extra features)
- combinationWithExtraFeatures_label.csv: a final dynamic callgraph use for training (include extra features, label field)
- raw_cg.csv: raw static callgraph in csv format directly parse from soot static callgraph
- refine_cg.csv: a static callgraph parse from raw_cg.csv (no extra features)
- labeled_cg.csv: a dynamic callgraph before process to generating extra feature (no extra features)

## Finding Source Code from Method Signature
- run `java -jar MethodSourceCodeRetriever.jar <methodSignaturesFile> <sourceFilesDirectory> <outputCsvFile>`