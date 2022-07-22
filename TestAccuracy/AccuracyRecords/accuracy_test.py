import json
from statistics import mean, median, stdev

class Position:
    def __init__(self, x, y):
        self.x = x
        self.y = y
    
    def __hash__(self):
        return hash((self.x, self.y))

    def __eq__(self, other):
        return (self.x, self.y) == (other.x, other.y)

    def __repr__(self) -> str:
        return "({},{})".format(self.x, self.y)

def load_json_data(file):
    data = ""
    with open(file, "r") as f:
        data = f.read()
    return json.loads(data)

"""
    Parse accuracy_records.json and print out the result
"""
def print_accuracy_result():
    json_array = load_json_data("accuracy_records.json")
    # key: position, value: list of errors
    position_error_map = dict()
    for json_obj in json_array:
        x = json_obj.get("ref_x")
        y = json_obj.get("ref_y")
        error = json_obj.get("error")
        pos = Position(x, y)
        if position_error_map.get(pos) is None:
            position_error_map[pos] = [error]
        else:
            position_error_map[pos].append(error)
    # list to store all the errors
    all_error = []
    # traverse the map and calculate the mean and standard deviation for each position
    for pos in position_error_map.keys():
        error_list = position_error_map[pos]
        all_error += error_list
        avg_error = mean(error_list)
        std_error = stdev(error_list)
        median_error = median(error_list)
        min_error = min(error_list)
        max_error = max(error_list)
        print("{} mean error: {:.2f} m, stdev: {:.2f} m, median: {:.2f} m, min: {:.2f} m, max: {:.2f} m".format(pos, avg_error, std_error, median_error, min_error, max_error))
    print("Overall, the mean error is {:.2f} m and stdev is {:.2f} m".format(mean(all_error), stdev(all_error)))

if __name__ == "__main__":
    print_accuracy_result()
