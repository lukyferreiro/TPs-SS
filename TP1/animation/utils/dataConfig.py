def check_int(num, str):
   if not type(num) == int or num < 0:
      raise ValueError(f"Valor de '{str}' invalido")
   
   return num

def check_float(num, str):
   if not type(num) == float and num < 0:
      raise ValueError(f"Valor de '{str}' invalido")
   
   return num
   
class DataConfig:

    def __init__(self, data):
      self.staticFile = data['staticFile']
      self.dynamicFile = data['dynamicFile']
      self.outNeighborsFile = data['outNeighborsFile']
      self.outTimeFile = data['outTimeFile']
      self.method = data['method']
      self.isPeriodic = data['isPeriodic']
      self.Rc = check_float(data['Rc'], "Rc")
      self.M = check_int(data['M'], "M")

      with open(f"../simulator/{self.staticFile}", 'r') as static:
        self.N = int(static.readline().strip())
        self.L = float(static.readline().strip())

      #self.N = check_int(data['N'], "N")
      #self.L = check_float(data['L'], "L")
      #self.max_radius = check_float(data['max-radius'], "max_radius")
      #self.min_radius = check_float(data['min-radius'], "min_radius")
      #self.times = check_int(data['times'], "times")