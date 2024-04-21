import numpy as np
from sklearn.datasets import load_iris
from sklearn.preprocessing import OneHotEncoder
from sklearn.model_selection import train_test_split
from scipy.optimize import differential_evolution

class MLP:
    def __init__(self, input_size, hidden_size, output_size):
        self.input_size = input_size
        self.hidden_size = hidden_size
        self.output_size = output_size
        
        # initialize weights
        self.weights_input_hidden = np.random.rand(self.input_size, self.hidden_size)
        self.weights_hidden_output = np.random.rand(self.hidden_size, self.output_size)
        
        # initialize biases
        self.bias_hidden = np.random.rand(self.hidden_size)
        self.bias_output = np.random.rand(self.output_size)
    
    def forward(self, X):
        # input layer to hidden layer
        hidden_input = np.dot(X, self.weights_input_hidden) + self.bias_hidden
        hidden_output = self.sigmoid(hidden_input)
        
        # hidden layer to output layer
        output_input = np.dot(hidden_output, self.weights_hidden_output) + self.bias_output
        output = self.sigmoid(output_input)
        
        return output
    
    def sigmoid(self, X):
        return 1 / (1 + np.exp(-X))
    
    def predict(self, X):
        return np.argmax(self.forward(X), axis=1)

def loss_function(weights, X, y, input_size, hidden_size, output_size):
    # Entpacken der Gewichte und Schwellenwerte

    
    # MLP initialisieren
    mlp = MLP(input_size, hidden_size, output_size)

    
    # Vorhersage für die Eingabe X berechnen
    output = mlp.forward(X)
    
    # Fehler berechnen
    error = y - output
    
    # Loss berechnen
    loss = np.mean(error**2)
    
    return loss

# Daten laden
iris = load_iris()
X = iris.data
y = iris.target

# One-hot encoding der Ausgabe


# Aufteilung in Trainings- und Testdaten
X_train, X_test, y_train, y_test = train_test_split(X, y, test_size=0.2)

# Trainingsfunktion mit Differential Evolution


result = differential_evolution(loss_function, ...)

# Ausgabe der Ergebnisse
print("Optimale Gewichte und Schwellenwerte:", result.x)
print("Loss:", result.fun)

# MLP-Modell mit den optimalen Gewichten und Schwellenwerten initialisieren


# Vorhersage auf den Testdaten durchführen
y_pred = mlp.predict(X_test)
print("Accuracy:", np.mean(y_pred == np.argmax(y_test, axis=1)))