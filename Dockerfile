FROM gradle:8.1.1-jdk17

WORKDIR /

COPY / .

RUN gradle installDist

CMD ./build/install/demo/bin