import numpy as np
import matplotlib.pyplot as plt
import pandas as pd
from Position import Position
from accuracy_test import get_position_error_map

position_error_map = get_position_error_map('accuracy_records_joe_model.json', True)

data = position_error_map[Position(5,4)]

# getting data of the histogram
count, bins_count = np.histogram(data, bins=10)
  
# finding the PDF of the histogram using count values
pdf = count / sum(count)
  
# using numpy np.cumsum to calculate the CDF
# We can also find using the PDF values by looping and adding
cdf = np.cumsum(pdf)
  
# plotting PDF and CDF
# plt.plot(bins_count[1:], pdf, color="red", label="PDF")
plt.plot(bins_count[1:], cdf, label="CDF")
plt.legend()

plt.show()