import argparse
import matplotlib.pyplot as plt


def run():
    parser = argparse.ArgumentParser(description='Create virtual eye-diagram from lis file.')
    parser.add_argument('-f', '--lis-file', metavar='[file]', type=str,
                        help='Lis file of single shot pulse.')
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
    eye_lines = get_eye_lines(vlist, eye_size, args.resolution)
    for line in eye_lines:
        plt.plot([x * args.resolution * 1000000000 for x in range(eye_size)], line, '-', linewidth=2, color='k')
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


# Create virtual eye diagram.
def get_eye_lines(vlist, eye_size, resolution):
    eye_lines = [[0.0] * eye_size for l in range(4)]
    max_volts = [0.0] * eye_size
    for i in range(len(vlist)):
        eye_pos = i % eye_size
        point_val = vlist[i]
        if point_val > 0.0:
            eye_lines[0][eye_pos] += point_val
        else:
            eye_lines[3][eye_pos] += point_val
        if point_val > max_volts[eye_pos]:
            max_volts[eye_pos] = point_val

    max_volt = 0.0
    eye_start = 0
    for i in range(eye_size):
        eye_lines[1][i] = eye_lines[0][i] - max_volts[i]
        eye_lines[2][i] = eye_lines[3][i] + max_volts[i]
        if (eye_lines[1][i] > max_volt):
            max_volt = eye_lines[1][i]
            eye_start = i

    # Shift eye diagram.
    for i in range(len(eye_lines)):
        eye_lines[i] = eye_lines[i][eye_start:] + eye_lines[i][:eye_start]

    # Print eye size info.
    min_volt = float("inf")
    eye_open_max = 0
    eye_open_min = 0
    eye_open_start = 0
    eye_open_end = 0
    for i in range(eye_size):
        if (eye_lines[1][i] < min_volt):
            min_volt = eye_lines[1][i]
            eye_open_max = eye_lines[2][i]
            eye_open_min = eye_lines[1][i]
        if (eye_lines[1][i] < eye_lines[2][i] and eye_open_start == 0):
            eye_open_start = i
        if (eye_lines[1][i] >= eye_lines[2][i] and eye_open_start != 0):
            eye_open_end = i
    print('Eye width: ' + str((eye_open_end - eye_open_start) * resolution))
    print('Eye height: ' + str(eye_open_max - eye_open_min))

    return eye_lines


if __name__ == '__main__':
    run()
