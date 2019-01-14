# STL scripts

This is sample commands of the scripts.

## Score graph

Write the average score graph from stldesigner result. This command can plot multiple result.

```
// Create stldesigner result, if you have not run before.
$ mvn "run ./data/config/default.yml"
$ python score_graph.py -g 4000 -d '[{"tag":"result", "path":"../output/eye_size_multi_thread"}]'
```

## Segment graph

Write STL segment structure from sp file.

```
$ python segment_graph.py -f ./spice_data/template_W_ga_best.sp
```

## Normal eye-diagram

Write normal eye-diagram from lis file containing random bit pulse output.

```
$ python normal_eye.py -f ./spice_data/template_W_random.lis --eye-time 2e-9 --opt-point optpt3 --resolution 5e-12 --shift-ratio 0.8
```

## Virtual eye-diagram

Write virtual eye-diagram from lis file containing shingle shot pulse output.

```
$ python virtual_eye.py -f ./spice_data/template_W.lis --eye-time 2e-9 --opt-point optpt3 --resolution 5e-12                       
```
