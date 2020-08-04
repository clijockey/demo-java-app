


```
gcloud config set run/region REGION
```

Cloud Run on Anthos
```
gcloud config set run/cluster CLUSTER-NAME
gcloud config set run/cluster_location REGION

```

Cloud Run;
```
gcloud run deploy SERVICE-NAME \
    --image gcr.io/PROJECT/IMAGE \
    --platform managed
```