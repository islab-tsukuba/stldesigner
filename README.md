# STLDesigner

STLDesigner is a optimizer for Segmental Transmission Line(STL)

## Requirements
* [sbt](www.scala-sbt.org)
* HSPICE

## Usage

Run test.

```
$ sbt test

```

Optimize STL.
```
$ sbt "run ./data/config/default.yml"

```

## Tools
Tools is written by python3 code. It's a setup commands using venv.

```
$ cd tools
$ python3 -m venv venv
$ source ./venv/bin/activate
$ pip install -r requirements.txt
```

After setup, you can run scripts in tools directory.

```
$ python score_graph.py -h
```

Sample command is written in tools/README.md.

## Related work
* [Signal integrity improvement in lossy transmission line using segmental transmission line](https://ieeexplore.ieee.org/document/6469402)
* [Evolutionary design of high signal integrity interconnection based on eye-diagram](https://link.springer.com/article/10.1007/s10015-018-0433-2)
