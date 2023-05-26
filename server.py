from flask import Flask, request, jsonify
from sentence_transformers import SentenceTransformer
from transformers import AutoTokenizer, AutoModel
import torch
import torch.nn.functional as F
import logging

MODEL_NAME = 'sentence-transformers/all-mpnet-base-v2'

logging.basicConfig(level=logging.INFO)
app = Flask(__name__)
tokenizer = AutoTokenizer.from_pretrained(MODEL_NAME)
model = AutoModel.from_pretrained(MODEL_NAME)

def mean_pooling(model_output, attention_mask):
    token_embeddings = model_output[0]
    input_mask_expanded = attention_mask.unsqueeze(-1).expand(token_embeddings.size()).float()
    return torch.sum(token_embeddings * input_mask_expanded, 1) / torch.clamp(input_mask_expanded.sum(1), min=1e-9)

def generate_embeddings(sentences):
    encoded_input = tokenizer(sentences, padding=True, truncation=True, return_tensors='pt')

    with torch.no_grad():
        model_output = model(**encoded_input)

    sentence_embeddings = mean_pooling(model_output, encoded_input['attention_mask'])
    sentence_embeddings = F.normalize(sentence_embeddings, p=2, dim=1)
    return sentence_embeddings.tolist()

def validate_sentences(sentences):
    if not isinstance(sentences, list):
        return False
    for sentence in sentences:
        if not isinstance(sentence, str) or not sentence:
            return False
    return True

@app.route('/embeddings', methods=['POST'])
def generate_embeddings_controller():
    data = request.get_json();
    logging.info(f"Received request to generate embeddings for sentences: {data}")
    sentences = data.get('sentences', [])

    if not validate_sentences(sentences):
        return jsonify({'error': 'Invalid request parameters'}), 400

    if not sentences:
        return jsonify({'error': 'No sentences provided'}), 400

    response = {'embeddings': generate_embeddings(sentences)}
    logging.info("Sentence embeddings generated successfully")
    return jsonify(response)

if __name__ == '__main__':
    app.run()
