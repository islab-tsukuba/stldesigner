# STLDesigner4

STLDesigner is a optimizer for Segmental Transmission Line(STL)

## Requirements
* Apache maven
* HSPICE

## Usage

Run test.

```
$ mvn test

```

Optimize STL.
```
$ mvn "run ./data/config/default.yml"

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
$ python create_graph.py
```

Sample command is written in tools/README.md.
