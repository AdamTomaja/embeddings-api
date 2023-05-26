# Sentence Embedding Server

The Sentence Embedding Server is a REST API that generates sentence embeddings using the Sentence Transformers library and the All-MPNet-base-v2 model from the Hugging Face Transformers library. It provides a simple and efficient way to encode sentences into dense vector representations, which can be useful for various natural language processing (NLP) tasks such as semantic search, text similarity, clustering, and more.

## Features

- Generates sentence embeddings using the Sentence Transformers library and the All-MPNet-base-v2 model.
- Supports multiple sentence inputs, allowing you to pass a single sentence or a list of sentences.
- Provides a REST API endpoint for easy integration into your applications.
- Includes a Dockerfile for containerization, enabling quick and consistent deployment across different environments.

## Dependencies

To run the Sentence Embedding Server, ensure that you have the following dependencies installed:

- Python 3.9 or later
- sentence-transformers
- transformers
- torch
- flask

## Installation

1. Clone this repository to your local machine:

```shell
git clone https://github.com/AdamTomaja/embeddings-api.git
```

2. Change into the project directory:

```shell
cd sentence-embedding-server
```

3. Install the required Python dependencies using pip:
```shell
pip install -r requirements.txt
```

## Usage
1. Start the server by running the following command:

```shell
python server.py
```
The Flask server will start running on http://localhost:5000.

2. To generate sentence embeddings, send a POST request to http://localhost:5000/embeddings with the following JSON payload:

```json
{
  "sentences": [
    "This is sentence 1.",
    "Another example sentence."
  ]
}
```
Example response:
```json
{
    "embeddings": [
        [
          0.03552525117993355,
          -0.0064552645199000835,
          -0.017893081530928612,
          -0.004641285166144371, ...
        ],
        [
          0.048065587878227234,
          0.017401790246367455,
          0.0060195038095116615,
          -0.02639615908265114, ...
        ]
    ]
}
```
The server will respond with a JSON object containing the sentence embeddings.

## Docker Support
The Sentence Embedding Server can also be run in a Docker container. To build and run the Docker image, follow these steps:

1.Build the Docker image using the provided Dockerfile:
```shell
docker build -t embedding-api .
```

2. Run the Docker container:
```shell
docker run -p 5000:5000 embedding-api
```
The Flask server will be accessible at http://localhost:5000 within the Docker container, and the container's port 5000 will be mapped to your local machine's port

## Contributing
Contributions are welcome! If you find any issues or have suggestions for improvements, please open an issue or submit a pull request on the GitHub repository.