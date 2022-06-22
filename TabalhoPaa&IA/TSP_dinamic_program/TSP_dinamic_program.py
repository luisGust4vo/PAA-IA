import time
import numpy as np
from math import sqrt
np.random.seed(42)

import matplotlib.pyplot as plt
%matplotlib inline

def DP_TSP(distances_array):
    n = len(distances_array)
    all_points_set = set(range(n))

    memo = {(tuple([i]), i): tuple([0, None]) for i in range(n)}
    queue = [(tuple([i]), i) for i in range(n)]

    print(queue)

    while queue:
        prev_visited, prev_last_point = queue.pop(0)
        prev_dist, _ = memo[(prev_visited, prev_last_point)]

        to_visit = all_points_set.difference(set(prev_visited))
        for new_last_point in to_visit:
            new_visited = tuple(sorted(list(prev_visited) + [new_last_point]))
            new_dist = prev_dist + distances_array[prev_last_point][new_last_point]

            if (new_visited, new_last_point) not in memo:
                memo[(new_visited, new_last_point)] = (new_dist, prev_last_point)
                queue += [(new_visited, new_last_point)]
            else:
                if new_dist < memo[(new_visited, new_last_point)][0]:
                    memo[(new_visited, new_last_point)] = (new_dist, prev_last_point)

    optimal_path, optimal_cost = retrace_optimal_path(memo, n)

    return optimal_path, optimal_cost

def retrace_optimal_path(memo: dict, n: int) -> [[int], float]:
    points_to_retrace = tuple(range(n))

    full_path_memo = dict((k, v) for k, v in memo.items() if k[0] == points_to_retrace)
    path_key = min(full_path_memo.keys(), key=lambda x: full_path_memo[x][0])

    last_point = path_key[1]
    optimal_cost, next_to_last_point = memo[path_key]

    optimal_path = [last_point]
    points_to_retrace = tuple(sorted(set(points_to_retrace).difference({last_point})))

    while next_to_last_point is not None:
        last_point = next_to_last_point
        path_key = (points_to_retrace, last_point)
        _, next_to_last_point = memo[path_key]

        optimal_path = [last_point] + optimal_path
        points_to_retrace = tuple(sorted(set(points_to_retrace).difference({last_point})))

    return optimal_path, optimal_cost

def plot_route(X, optimal_path):
    for p1, p2 in zip(optimal_path[:-1], optimal_path[1:]):
        plt.plot([X[p1, 0], X[p2, 0]], [X[p1, 1], X[p2, 1]]);

def calculateCityDistances(cityArray):
  distancesArray = []

  for city in cityArray:
    distCity = []
    for targetCity in cityArray:
      distCityAndTarget = sqrt((city[0]-targetCity[0])**2) + ((city[1]-targetCity[1])**2)
      distCity.append(distCityAndTarget)
    
    distancesArray.append(distCity)
  
  return distancesArray

def readInputFile(fileName): 
  with open(fileName) as f:
    array = []
    n = int(next(f)) 
    
    z = 0
    for line in f: 
      a = []
      i = 0
      for x in line.split() :
        a.append(int(x))
        i = i+1
      array.append(a)
      z = z+1

    return np.array(array)

X = readInputFile('entrada.txt')
distances_array = calculateCityDistances(X)

t = time.time()
optimal_path, optimal_cost = DP_TSP(distances_array)
runtime = round(time.time() - t, 3)

print(f"Encontrou o caminho ideal em {runtime} segundos.") 
print(f"Custo ótimo: {round(optimal_cost, 3)}, caminho ótimo: {optimal_path}") 

plt.figure(figsize=(10, 5));

plt.subplot(1, 2, 1);
plt.scatter(X[:, 0], X[:, 1]);

plt.subplot(1, 2, 2);
plot_route(X, optimal_path)

plt.tight_layout(); 