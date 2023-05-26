# Use the official Python base image
FROM python:3.9-slim

# Set the working directory in the container
WORKDIR /app

# Copy the requirements file
COPY requirements.txt .

# Install the required dependencies
RUN pip install --no-cache-dir -r requirements.txt

# Copy the script file to the working directory
COPY server.py .

# Expose the port the Flask app runs on
EXPOSE 5000

# Set the environment variable for Flask
ENV FLASK_APP=server.py

# Run the Flask application
CMD ["flask", "run", "--host=0.0.0.0"]
