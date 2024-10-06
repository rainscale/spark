import sys
from PySide6.QtWidgets import QApplication, QWidget, QVBoxLayout, QPushButton, QLineEdit

class MyApp(QWidget):
    def __init__(self):
        super().__init__()
        self.init_ui()

    def init_ui(self):
        layout = QVBoxLayout()
        self.input_box = QLineEdit(self)
        layout.addWidget(self.input_box)
        self.button = QPushButton('Print Input', self)
        layout.addWidget(self.button)
        self.button.clicked.connect(self.on_button_clicked)
        self.setLayout(layout)
        self.setWindowTitle('Input Printer')
        self.show()

    def on_button_clicked(self):
        input_text = self.input_box.text()
        print(input_text)

if __name__ == '__main__':
    app = QApplication(sys.argv)
    my_app = MyApp()
    sys.exit(app.exec())
