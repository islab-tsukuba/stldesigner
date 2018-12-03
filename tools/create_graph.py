import matplotlib.pyplot as plt
import argparse
import json
import os
import re


def run():
    parser = argparse.ArgumentParser(description='Create graph from multiple output.')
    parser.add_argument('-d', '--dirs', metavar='[json]', type=str,
                    help='Directory of the stldesigner output. ' +
                         'Example:[{"tag":"default", "path":"../output/default"}]')
    parser.add_argument('-g', '--plot-gen', metavar='N', type=int, required=True,
                    help='Generation num of plot.')
    parser.add_argument('--eps', action='store_true', help='Output to eps file.')

    args = parser.parse_args()

    json_list = json.loads(args.dirs)
    for item in json_list:
        tag = item['tag']
        path = item['path']
        sum_list = [0.0] * args.plot_gen
        dirs = get_stl_child_dirs(path)
        child_num = len(dirs)
        for c in dirs:
            score_list = read_best_scores(os.path.join(c, "best.csv"), args.plot_gen)
            for i in range(args.plot_gen):
                sum_list[i] += score_list[i]
        average_list = [s / child_num for s in sum_list]
        plt.plot([x + 1 for x in range(args.plot_gen)], average_list, '-', label=tag, linewidth=1)
        print("[" + tag + "] Best score: " + str(average_list[len(average_list)-1]))
    plt.legend(loc='upper right', shadow=True, fontsize='x-large')
    plt.yscale("log")
    if args.eps:
        plt.savefig("out.eps")
    else:
        plt.show()


def read_best_scores(path, plot_gen):
    score_list = [0.0] * plot_gen
    f = open(path)
    header_items = split_csv(f.readline())
    gen_index = header_items.index('gen')
    score_index = header_items.index('score')
    line = f.readline()
    last_gen = 0
    last_score = 1.0
    while line:
        items = split_csv(line)
        gen = int(items[gen_index])
        score = float(items[score_index])
        if len(score_list) < gen - 1:
            break
        for i in range(last_gen - 1, gen - 1):
            score_list[i] = last_score
        last_gen = gen
        last_score = score
        line = f.readline()
    for i in range(last_gen - 1, len(score_list)):
        score_list[i] = last_score

    return score_list


def split_csv(str):
    return [item.strip() for item in str.split(',')]


def get_stl_child_dirs(path):
    dirs = []
    for x in os.listdir(path):
        child = os.path.join(path, x)
        if os.path.isdir(child):
            if is_stl_dir(child):
                dirs.append(child)
    return dirs


def is_stl_dir(path):
    return re.match(r".*id_\d+$", path) is not None


if __name__ == '__main__':
    run()

