import argparse
import matplotlib.pyplot as plt


def run():
    parser = argparse.ArgumentParser(description='Create virtual eye-diagram from lis file.')
    parser.add_argument('-f', '--lis-file', metavar='[file]', type=str,
                        help='Lis file of random bit pulse.')
    parser.add_argument('--eye-time', metavar='[time]', type=float, required=True,
                        help='Frequency of input pulse specified in sec. It will be eye width. Example: 2e-9')
    parser.add_argument('--resolution', metavar='[time]', type=float, required=True,
                        help='Resolution of spice simulation. Example: 5e-12')
    parser.add_argument('--opt-point', metavar='[point]', type=str, required=True,
                        help='Optimize point of output file.')
    parser.add_argument('--eps', metavar='[file]', type=str, required=False, default="",
                        help='Output EPS file name. If it is no specified, file is not outputted.')

    args = parser.parse_args()

    start_value = False
    val_names = []
    val_list = []
    with open(args.lis_file) as f:
        line = f.readline()
        while line:
            if line.strip() == 'y':
                break
            if start_value:
                val_list.append(parse_vals_to_float(line.split()))
            if line.strip() == 'x':
                # Skip header
                f.readline()
                f.readline()
                line = f.readline()
                val_names = line.split()
                start_value = True
            line = f.readline()

    opt_index = val_names.index(args.opt_point) + 1
    vlist = [vals[opt_index] for vals in val_list]
    eye_size = int(args.eye_time / args.resolution)
    for i in range(int(len(vlist) / eye_size)):
        plt.plot([x * args.resolution for x in range(eye_size)],
                 vlist[i * eye_size:(i + 1) * eye_size], '-', linewidth=2, color='b')
    ax = plt.axes()
    ax.set_xlabel('Time [ns]')
    ax.set_ylabel('Voltage [V]')
    if args.eps != '':
        plt.savefig(args.eps)
    else:
        plt.show()


def parse_vals_to_float(vals):
    float_vals = []
    for val in vals:
        val = val.replace('c', 'e-2')
        val = val.replace('m', 'e-3')
        val = val.replace('u', 'e-6')
        val = val.replace('n', 'e-9')
        val = val.replace('p', 'e-12')
        val = val.replace('f', 'e-15')
        float_vals.append(float(val))
    return float_vals


if __name__ == '__main__':
    run()
